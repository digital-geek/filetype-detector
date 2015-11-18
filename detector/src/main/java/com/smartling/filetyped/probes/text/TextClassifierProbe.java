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
package com.smartling.filetyped.probes.text;

import com.smartling.filetyped.model.IdentifyResponse;
import com.smartling.filetyped.model.ResponseConverter;
import com.smartling.ml.ClassifierResult;
import com.smartling.filetyped.probes.Probe;
import java.io.IOException;
import java.util.Optional;

public class TextClassifierProbe implements Probe<String>
{
    private final TextClassifier classifier;

    private final ResponseConverter responseConverter;

    public TextClassifierProbe()
    {
        this(new TextClassifier(), new ResponseConverter());
    }

    public TextClassifierProbe(TextClassifier classifier, final ResponseConverter responseConverter)
    {
        this.classifier = classifier;
        this.responseConverter = responseConverter;
    }

    @Override
    public Optional<IdentifyResponse> detect(final String fileName, final String content) throws IOException
    {
        ClassifierResult classifierResult = classifier.classify(content);

        IdentifyResponse response = responseConverter.convert(fileName, classifierResult);

        return Optional.of(response);
    }
}
