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
package com.smartling.filetyped.service.probes.xml;

import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.ml.text.TextSearchEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class XmlTextProcessor
{
    private static final Map<String, ContentFileType> IDENTIFYING_KEYWORDS = new HashMap<>();

    private static final int TAGS_THRESHOLD = 5;

    private static final Pattern SL_DIRECTIVE = Pattern.compile("smartling\\.translate_paths", Pattern.CASE_INSENSITIVE);

    private static final Pattern TAGS_MARKER = Pattern.compile("^\\s*<[^ \\d.-]");

    static
    {
        IDENTIFYING_KEYWORDS.put("<xliff ", ContentFileType.XLIFF);
        IDENTIFYING_KEYWORDS.put("<resheader ", ContentFileType.RESX);
        IDENTIFYING_KEYWORDS.put("<tmx ", ContentFileType.TMX);
        IDENTIFYING_KEYWORDS.put("<tmx:", ContentFileType.TMX);
        IDENTIFYING_KEYWORDS.put("<resources", ContentFileType.ANDROID);
        IDENTIFYING_KEYWORDS.put("<TS ", ContentFileType.QT);
        IDENTIFYING_KEYWORDS.put("NSStringLocalizedFormatKey", ContentFileType.STRINGSDICT);
        IDENTIFYING_KEYWORDS.put("<dict>", ContentFileType.STRINGSDICT);
    }

    private final TextSearchEngine searchEngine;

    public XmlTextProcessor()
    {
        this(new TextSearchEngine());
    }

    public XmlTextProcessor(TextSearchEngine searchEngine)
    {
        this.searchEngine = searchEngine;
    }

    public XmlTextFeatures process(String document)
    {
        XmlTextFeaturesBuilder features = new XmlTextFeaturesBuilder();

        searchEngine.addKeyword("<");
        searchEngine.addKeyword(">");

        for (String keyword : IDENTIFYING_KEYWORDS.keySet())
            searchEngine.addKeyword(keyword);

        Map<String, Integer> searchCount = searchEngine.search(document);

        for (String keyword : IDENTIFYING_KEYWORDS.keySet())
        {
            if (searchCount.get(keyword) > 0)
            {
                ContentFileType fileType = IDENTIFYING_KEYWORDS.get(keyword);
                features.setDetectedFileType(fileType);
                break;
            }
        }

        int tagCount = Math.min(searchCount.get("<"), searchCount.get(">"));

        if (tagCount >= TAGS_THRESHOLD)
        {
            if (TAGS_MARKER.matcher(document).find())
                features.setHasTags(true);
        }

        if (SL_DIRECTIVE.matcher(document).find())
        {
            features.setHasSmartlingDirectives(true);
        }

        return features.build();
    }

}
