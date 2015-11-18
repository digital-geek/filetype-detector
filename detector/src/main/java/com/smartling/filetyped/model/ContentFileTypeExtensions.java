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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;

public class ContentFileTypeExtensions
{
    static
    {
        typeExtensions = new HashMap<>();

        addExtensions(ContentFileType.JAVA_PROPERTIES, ".properties");
        addExtensions(ContentFileType.IOS, ".strings");
        addExtensions(ContentFileType.STRINGSDICT, ".stringsdict");
        addExtensions(ContentFileType.GETTEXT, ".po", ".pot");
        addExtensions(ContentFileType.RESX, ".resx", ".resw");
        addExtensions(ContentFileType.XLIFF, ".xlf", ".xliff");
        addExtensions(ContentFileType.YAML, ".yml", ".yaml");
        addExtensions(ContentFileType.JSON, ".json", ".js");
        addExtensions(ContentFileType.XML, ".svg"); // excluding generic ".xml"
        addExtensions(ContentFileType.HTML, ".html", ".htm");
        addExtensions(ContentFileType.DOCX, ".docx");
        addExtensions(ContentFileType.PPTX, ".pptx");
        addExtensions(ContentFileType.XLSX, ".xlsx");
        addExtensions(ContentFileType.IDML, ".idml");
        addExtensions(ContentFileType.XLS, ".xls");
        addExtensions(ContentFileType.DOC, ".doc");
        addExtensions(ContentFileType.QT, ".ts");
        addExtensions(ContentFileType.CSV, ".csv");
        addExtensions(ContentFileType.TMX, ".tmx");
        addExtensions(ContentFileType.PLAIN_TEXT, ".txt");
        addExtensions(ContentFileType.PPT, ".ppt");
        addExtensions(ContentFileType.PRES, ".pres");
        addExtensions(ContentFileType.MADCAP); // excluding generic ".zip"
    }

    private static final Map<String, ContentFileType> typeExtensions;

    private static void addExtensions(ContentFileType fileType, String... extensions)
    {
        for (String fileExtension : extensions)
        {
            assert !typeExtensions.containsKey(fileExtension) : "duplicate key found: " + fileExtension;
            typeExtensions.put(fileExtension, fileType);
        }
    }

    public Optional<ContentFileType> get(String fileName)
    {
        String fileExtension = ("." + FilenameUtils.getExtension(fileName)).toLowerCase();
        ContentFileType fileType = typeExtensions.get(fileExtension);
        return Optional.ofNullable(fileType);
    }
}
