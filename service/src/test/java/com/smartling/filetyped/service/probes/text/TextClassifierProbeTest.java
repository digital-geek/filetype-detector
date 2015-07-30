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
import com.smartling.filetyped.dto.ResponseConverter;
import com.smartling.ml.ClassifierResult;
import java.io.IOException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TextClassifierProbeTest
{
    @Mock
    private TextClassifier classifier;

    @Mock
    private ResponseConverter responseConverter;

    @Mock
    private ClassifierResult classifierResult;

    @Mock
    private IdentifyResponse identifyResponse;

    @InjectMocks
    private TextClassifierProbe probe;

    @Before
    public void setUp() throws IOException
    {
        MockitoAnnotations.initMocks(this);

        when(classifier.classify(any())).thenReturn(classifierResult);
        when(responseConverter.convert(any(), any())).thenReturn(identifyResponse);
    }

    @Test
    public void callsClassifierWithContent() throws IOException
    {
        probe.detect("ignore", "some text");

        verify(classifier).classify("some text");
    }

    @Test
    public void convertsClassifierResponse() throws IOException
    {
        probe.detect("some file", "ignore");

        verify(responseConverter).convert("some file", classifierResult);
    }

    @Test
    public void returnsConvertedResponse() throws IOException
    {
        Optional<IdentifyResponse> response = probe.detect("ignore", "ignore");

        assertThat(response, is(Optional.of(identifyResponse)));
    }
}
