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

import com.smartling.ml.ClassifierResult;
import com.smartling.ml.features.FeatureExtractor;
import com.smartling.ml.features.Features;
import com.smartling.ml.SvmClassifier;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TextClassifierTest
{
    @Mock
    private FeatureExtractor featureExtractor;

    @Mock
    private SvmClassifier svmClassifier;

    @Mock
    private Features features;

    @Mock
    private ClassifierResult classifierResult;

    @InjectMocks
    private TextClassifier textClassifier;

    @Before
    public void setUp() throws IOException
    {
        MockitoAnnotations.initMocks(this);

        when(featureExtractor.extract(any())).thenReturn(features);
        when(svmClassifier.classify(any(), anyDouble())).thenReturn(classifierResult);
    }

    @Test
    public void callsFeatureExtractorOnContent() throws IOException
    {
        textClassifier.classify("some text");

        verify(featureExtractor).extract("some text");
    }

    @Test
    public void callsSvmClassifierOnFeatures() throws IOException
    {
        textClassifier.classify("ignore");

        verify(svmClassifier).classify(features, 0.15);
    }

    @Test
    public void returnsClassifierResult() throws IOException
    {
        ClassifierResult result = textClassifier.classify("ignore");

        assertThat(result, is(classifierResult));
    }
}
