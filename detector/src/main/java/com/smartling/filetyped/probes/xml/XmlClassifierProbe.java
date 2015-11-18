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
package com.smartling.filetyped.probes.xml;

import com.smartling.filetyped.model.ContentFileType;
import com.smartling.filetyped.model.IdentifyResponse;
import com.smartling.filetyped.probes.Probe;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class XmlClassifierProbe implements Probe<String>
{
    private static final float FULL_CONFIDENCE = 1f;

    private final XmlTextProcessor textProcessor;

    public XmlClassifierProbe()
    {
        this(new XmlTextProcessor());
    }

    public XmlClassifierProbe(final XmlTextProcessor textProcessor)
    {
        this.textProcessor = textProcessor;
    }

    @Override
    public Optional<IdentifyResponse> detect(final String fileName, final String content) throws IOException
    {
        XmlTextFeatures textStats = textProcessor.process(content);

        Optional<ContentFileType> detectedFileType = textStats.detectedFileType();

        if (detectedFileType.isPresent())
            return detectedType(fileName, detectedFileType.get(), FULL_CONFIDENCE);

        if (!textStats.hasTags())
            return Optional.empty();

        if (textStats.hasSmartlingDirectives())
            return detectedType(fileName, ContentFileType.XML, FULL_CONFIDENCE);

        return detectedType(fileName, ContentFileType.HTML, FULL_CONFIDENCE);
    }

    private Optional<IdentifyResponse> detectedType(final String fileName, final ContentFileType fileType, final float confidence)
    {
        IdentifyResponse response = new IdentifyResponse(fileName,
                Collections.singletonList(fileType),
                Collections.singletonList(confidence));
        return Optional.of(response);
    }
}
