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
package com.smartling.filetyped.dto;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ContentFileTypeExtensionsTest
{
    private ContentFileTypeExtensions typeExtensions = new ContentFileTypeExtensions();

    @Test
    public void returnsEmptyForEmpty()
    {
        checkGet("", Optional.empty());
        checkGet("some", Optional.empty());
    }

    private void checkGet(String value, Optional<ContentFileType> expected)
    {
        Optional<ContentFileType> returnedType = typeExtensions.get(value);
        assertThat(returnedType, is(expected));
    }

    @Test
    public void returnsJavaProperties()
    {
        checkGet("some.properties", ContentFileType.JAVA_PROPERTIES);
        checkGet(".properties", ContentFileType.JAVA_PROPERTIES);
    }

    private void checkGet(String value, ContentFileType expected)
    {
        checkGet(value, Optional.of(expected));
    }

    @Test
    public void ignoresCase()
    {
        checkGet("some.Properties", ContentFileType.JAVA_PROPERTIES);
        checkGet("some.PROPERTIES", ContentFileType.JAVA_PROPERTIES);
        checkGet("some.ProPerties", ContentFileType.JAVA_PROPERTIES);
    }

    @Test
    public void returnsIOS()
    {
        checkGet("some.strings", ContentFileType.IOS);
    }

    @Test
    public void returnsSTRINGSDICT()
    {
        checkGet("some.stringsdict", ContentFileType.STRINGSDICT);
    }

    @Test
    public void returnsGETTEXT()
    {
        checkGet("some.po", ContentFileType.GETTEXT);
        checkGet("some.pot", ContentFileType.GETTEXT);
    }

    @Test
    public void returnsRESX()
    {
        checkGet("some.resx", ContentFileType.RESX);
        checkGet("some.resw", ContentFileType.RESX);
    }

    @Test
    public void returnsXLIFF()
    {
        checkGet("some.xlf", ContentFileType.XLIFF);
        checkGet("some.xliff", ContentFileType.XLIFF);
    }

    @Test
    public void returnsYAML()
    {
        checkGet("some.yml", ContentFileType.YAML);
        checkGet("some.yaml", ContentFileType.YAML);
    }

    @Test
    public void returnsJSON()
    {
        checkGet("some.js", ContentFileType.JSON);
        checkGet("some.json", ContentFileType.JSON);
    }

    @Test
    public void returnsXML()
    {
        checkGet("some.svg", ContentFileType.XML);
    }

    @Test
    public void returnsHTML()
    {
        checkGet("some.html", ContentFileType.HTML);
        checkGet("some.htm", ContentFileType.HTML);
    }

    @Test
    public void returnsDOCX()
    {
        checkGet("some.docx", ContentFileType.DOCX);
    }

    @Test
    public void returnsPPTX()
    {
        checkGet("some.pptx", ContentFileType.PPTX);
    }

    @Test
    public void returnsXLSX()
    {
        checkGet("some.xlsx", ContentFileType.XLSX);
    }

    @Test
    public void returnsIDML()
    {
        checkGet("some.idml", ContentFileType.IDML);
    }

    @Test
    public void returnsXLS()
    {
        checkGet("some.xls", ContentFileType.XLS);
    }

    @Test
    public void returnsDOC()
    {
        checkGet("some.doc", ContentFileType.DOC);
    }

    @Test
    public void returnsQT()
    {
        checkGet("some.ts", ContentFileType.QT);
    }

    @Test
    public void returnsCSV()
    {
        checkGet("some.csv", ContentFileType.CSV);
    }

    @Test
    public void returnsTMX()
    {
        checkGet("some.tmx", ContentFileType.TMX);
    }

    @Test
    public void returnsPlainText()
    {
        checkGet("some.txt", ContentFileType.PLAIN_TEXT);
    }

    @Test
    public void returnsPPT()
    {
        checkGet("some.ppt", ContentFileType.PPT);
    }

    @Test
    public void returnsPRES()
    {
        checkGet("some.pres", ContentFileType.PRES);
    }
}
