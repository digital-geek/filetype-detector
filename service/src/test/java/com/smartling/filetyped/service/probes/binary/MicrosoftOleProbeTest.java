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
package com.smartling.filetyped.service.probes.binary;

import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.filetyped.dto.IdentifyResponse;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MagicBytes.class})
public class MicrosoftOleProbeTest
{
    @Mock
    private InputStream ignore;

    @Mock
    private MagicBytes magicBytes;

    @InjectMocks
    private MicrosoftOleProbe probe;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockStatic(MagicBytes.class);
        when(MagicBytes.of(any())).thenReturn(magicBytes);
    }

    @Test
    public void returnsEmptyIfNotMS() throws Exception
    {
        Optional<IdentifyResponse> response = probe.detect("some file", ignore);

        assertThat(response, is(Optional.<IdentifyResponse>empty()));
    }

    @Test
    public void detectsUnsupportedDoc() throws Exception
    {
        when(magicBytes.isMSOLE()).thenReturn(true);

        Optional<IdentifyResponse> response = probe.detect("some file", ignore);
        IdentifyResponse detectedType = response.get();

        assertThat(detectedType.getFileName(), is("some file"));
        assertThat(detectedType.getProbableTypes(), is(Collections.<ContentFileType>emptyList()));
        assertThat(detectedType.getConfidence(), is(Collections.<Float>emptyList()));
    }
}
