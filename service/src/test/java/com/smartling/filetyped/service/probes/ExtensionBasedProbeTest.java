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

import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.filetyped.dto.ContentFileTypeExtensions;
import com.smartling.filetyped.dto.IdentifyResponse;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExtensionBasedProbeTest
{
    @Mock
    private ContentFileTypeExtensions typeExtensions;

    @Mock
    private InputStream ignore;

    @InjectMocks
    private ExtensionBasedProbe probe;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        when(typeExtensions.get(anyString())).thenReturn(Optional.empty());
    }

    @Test
    public void detectsByExtension() throws Exception
    {
        when(typeExtensions.get(anyString()))
                .thenReturn(Optional.of(ContentFileType.CSV));

        Optional<IdentifyResponse> identifyResponse = probe.detect("some file", ignore);
        IdentifyResponse response = identifyResponse.get();

        verify(typeExtensions).get("some file");
        assertThat(response.getFileName(), is("some file"));
        assertThat(response.getProbableTypes(), is(Arrays.asList(ContentFileType.CSV)));
        assertThat(response.getConfidence(), is(Arrays.asList(1f)));
    }

    @Test
    public void returnsEmpty() throws Exception
    {
        Optional<IdentifyResponse> response = probe.detect("ignore", ignore);

        assertThat(response, is(Optional.empty()));
    }

    @Test
    public void discardsGenericPlainText() throws Exception
    {
        when(typeExtensions.get(anyString()))
                .thenReturn(Optional.of(ContentFileType.PLAIN_TEXT));

        Optional<IdentifyResponse> response = probe.detect("ignore", ignore);

        assertThat(response, is(Optional.empty()));
    }
}
