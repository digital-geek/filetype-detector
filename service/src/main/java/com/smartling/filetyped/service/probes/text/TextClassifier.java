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
import com.smartling.ml.SvmClassifier;
import com.smartling.ml.features.FeatureExtractor;
import com.smartling.ml.features.Features;
import java.io.IOException;

public class TextClassifier
{
    private static final double CONFIDENCE_THRESHOLD = 0.15;

    private final FeatureExtractor featureExtractor;

    private final SvmClassifier classifier;

    public TextClassifier()
    {
        this(new FeatureExtractor(), new SvmClassifier());
    }

    public TextClassifier(FeatureExtractor featureExtractor, SvmClassifier classifier)
    {
        this.featureExtractor = featureExtractor;
        this.classifier = classifier;
    }

    public ClassifierResult classify(final String content) throws IOException
    {
        Features features = featureExtractor.extract(content);

        return classifier.classify(features, CONFIDENCE_THRESHOLD);
    }
}
