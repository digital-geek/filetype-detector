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
package com.smartling.filetyped.service;

import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.filetyped.dto.IdentifyResponse;
import com.smartling.filetyped.service.probes.Probe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetectionServiceImplTest
{
    @InjectMocks
    private DetectionServiceImpl service;

    @Mock
    private Probe probe;

    @Mock
    private InputStream ignore;

    @Mock
    private IdentifyResponse expectedResponse;

    @Mock
    private RandomString randomString;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        when(probe.detect(any(), any())).thenReturn(Optional.empty());
    }

    @Test
    public void detectsNothing() throws Exception
    {
        IdentifyResponse response = service.identify("some file", ignore);

        assertThat(response.getFileName(), is("some file"));
        assertThat(response.getProbableTypes(), is(Collections.<ContentFileType>emptyList()));
        assertThat(response.getConfidence(), is(Collections.<Float>emptyList()));
    }

    @Test
    public void detectsSomething() throws Exception
    {
        when(probe.detect(any(), any()))
                .thenReturn(Optional.of(expectedResponse));

        IdentifyResponse response = service.identify("some file", ignore);

        assertThat(response, sameInstance(expectedResponse));
    }

    @Test
    public void generatesRandomReferenceId() throws IOException
    {
        service.identify("ignore", ignore);

        verify(randomString).random(8);
    }

    @Test
    public void setsReferenceIdOnEmptyResponse() throws IOException
    {
        when(randomString.random(anyInt()))
                .thenReturn("some random");

        IdentifyResponse response = service.identify("ignore", ignore);

        assertThat(response.getReferenceId(), is("some random"));
    }

    @Test
    public void setsReferenceIdOnResponse() throws IOException
    {
        when(randomString.random(anyInt()))
                .thenReturn("some random");
        when(probe.detect(any(), any()))
                .thenReturn(Optional.of(new IdentifyResponse("ignore", null, null)));

        IdentifyResponse response = service.identify("ignore", ignore);

        assertThat(response.getReferenceId(), is("some random"));
    }
}