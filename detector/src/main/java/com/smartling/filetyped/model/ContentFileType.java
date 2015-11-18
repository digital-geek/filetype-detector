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
package com.smartling.filetyped.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public enum ContentFileType
{
    JAVA_PROPERTIES,
    IOS,
    ANDROID,
    GETTEXT,
    PHP_RESOURCE,
    RESX,
    XLIFF,
    YAML,
    JSON,
    XML,
    HTML,
    FREEMARKER,
    DOCX,
    PPTX,
    XLSX,
    IDML,
    XLS,
    DOC,
    QT,
    CSV,
    TMX,
    PLAIN_TEXT,
    PPT,
    PRES,
    MADCAP,
    XLIFF_CAT,
    STRINGSDICT;

    private final static Map<String, ContentFileType> BY_NAME_LOOKUP = new HashMap<>();

    static
    {
        for (final ContentFileType value : ContentFileType.values())
        {
            BY_NAME_LOOKUP.put(value.nameLowerCamel.toLowerCase(), value);
            BY_NAME_LOOKUP.put(value.name().toLowerCase(), value);
        }
    }

    private final String nameLowerCamel;

    private ContentFileType()
    {
        nameLowerCamel = camelCase(name());
    }

    private static String camelCase(String value)
    {
        String caps = WordUtils.capitalizeFully(value, '_');
        String camel = WordUtils.uncapitalize(caps);
        return camel.replace("_", "");
    }

    @JsonValue
    @Override
    public String toString()
    {
        return nameLowerCamel;
    }

    public static Optional<ContentFileType> getByName(final String name)
    {
        ContentFileType value = BY_NAME_LOOKUP.get(StringUtils.lowerCase(name));
        return Optional.ofNullable(value);
    }
}
