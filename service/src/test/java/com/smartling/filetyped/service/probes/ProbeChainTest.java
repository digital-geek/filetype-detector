/*
 *  Copyright 2015 Smartling, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this work except in compliance with the License.
 *  You may obtain a copy of the License in the LICENSE file, or at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.smartling.filetyped.service.probes;

import com.smartling.filetyped.dto.IdentifyResponse;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProbeChainTest
{
    @Mock
    private Object ignore;

    @Mock
    private Probe<Object> probe1;

    @Mock
    private Probe<Object> probe2;

    private Optional<IdentifyResponse> expectedResponse;

    private ProbeChain<Object> chain;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        expectedResponse = Optional.of(mock(IdentifyResponse.class));
        when(probe1.detect(any(), any())).thenReturn(Optional.<IdentifyResponse>empty());
        when(probe2.detect(any(), any())).thenReturn(Optional.<IdentifyResponse>empty());
    }

    @Test
    public void detectsNothingWhenEmpty() throws Exception
    {
        chain = ProbeChain.of();

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, is(Optional.<IdentifyResponse>empty()));
    }

    @Test
    public void returnsSingleResult() throws Exception
    {
        when(probe1.detect(any(), any())).thenReturn(expectedResponse);
        chain = ProbeChain.of(probe1);

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, sameInstance(expectedResponse));
    }

    @Test
    public void returnsFirstNonEmpty() throws Exception
    {
        when(probe2.detect(any(), any())).thenReturn(expectedResponse);
        chain = ProbeChain.of(probe1, probe2);

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, sameInstance(expectedResponse));
    }
}
