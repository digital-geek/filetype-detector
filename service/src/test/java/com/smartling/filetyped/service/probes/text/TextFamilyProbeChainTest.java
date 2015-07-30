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
package com.smartling.filetyped.service.probes.text;

import com.smartling.filetyped.dto.IdentifyResponse;
import com.smartling.ml.text.ContentPreprocessor;
import com.smartling.filetyped.service.probes.Probe;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TextFamilyProbeChainTest
{
    @Mock
    private ContentPreprocessor preprocessor;

    @Mock
    private InputStream inputStream;

    @Mock
    private InputStream ignore;

    @Mock
    private Probe<String> probe1;

    @Mock
    private Probe<String> probe2;

    private Optional<IdentifyResponse> expectedResponse;

    @InjectMocks
    private TextFamilyProbeChain chain;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        chain.setChildren(probe1, probe2);
        expectedResponse = Optional.of(mock(IdentifyResponse.class));
        when(preprocessor.convert(any())).thenReturn(Optional.of("setup string"));
        when(probe1.detect(any(), any())).thenReturn(Optional.<IdentifyResponse>empty());
        when(probe2.detect(any(), any())).thenReturn(Optional.<IdentifyResponse>empty());
    }

    @Test
    public void convertsInputStreamWithPreprocessor() throws Exception
    {
        chain.detect("ignore", inputStream);

        verify(preprocessor).convert(inputStream);
    }

    @Test
    public void callsChildrenWithConvertedContent() throws Exception
    {
        when(preprocessor.convert(any())).thenReturn(Optional.of("some string"));

        chain.detect("some file", ignore);

        verify(probe1).detect("some file", "some string");
    }

    @Test
    public void detectsNothingWhenNoChildren() throws Exception
    {
        chain.setChildren();
        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, is(Optional.empty()));
    }

    @Test
    public void detectsNothingOfNonStringContent() throws Exception
    {
        when(preprocessor.convert(any())).thenReturn(Optional.empty());

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, is(Optional.empty()));
        verifyZeroInteractions(probe1);
        verifyZeroInteractions(probe2);
    }

    @Test
    public void returnsSingleResult() throws Exception
    {
        chain.setChildren(probe1);
        when(probe1.detect(any(), any())).thenReturn(expectedResponse);

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, sameInstance(expectedResponse));
    }

    @Test
    public void returnsFirstNonEmpty() throws Exception
    {
        when(probe2.detect(any(), any())).thenReturn(expectedResponse);

        Optional<IdentifyResponse> response = chain.detect("ignore", ignore);

        assertThat(response, sameInstance(expectedResponse));
    }

}
