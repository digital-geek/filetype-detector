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
package com.smartling.filetyped;

import com.smartling.filetyped.model.ContentFileType;
import com.smartling.filetyped.model.IdentifyResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DetectionServiceTest
{
    private DetectionService service = new DetectionService();

     private void checkDocumentTypeExact(String contentString, ContentFileType fileType, float confidence) throws IOException
     {
         checkDocumentTypeExact("some file", contentString, fileType, confidence);
     }
     private void checkDocumentTypeExact(String fileName, String contentString, ContentFileType fileType, float confidence) throws IOException
     {
         try (InputStream data = new ByteArrayInputStream(contentString.getBytes("UTF-8")))
         {
             IdentifyResponse typeResponse = service.identify(fileName, data);

             assertThat(typeResponse.getFileName(), is(fileName));
             assertThat(typeResponse.getProbableTypes(), is(Collections.singletonList(fileType)));
             assertThat(typeResponse.getProbableTypes().size(), is(typeResponse.getConfidence().size()));
             assertThat(typeResponse.getConfidence(), is(Collections.singletonList(confidence)));
         }
     }

     //<editor-fold desc="Tests: Identify by extension">

     @Test
     public void identifiesByExtensionJavaProperties() throws IOException
     {
         checkDocumentTypeExact("some.properties", "ignore", ContentFileType.JAVA_PROPERTIES, 1f);
     }

     @Test
     public void identifiesByExtensionIOSstrings() throws IOException
     {
         checkDocumentTypeExact("some.strings", "ignore", ContentFileType.IOS, 1f);
     }

     @Test
     public void identifiesByExtensionIOSstringsdict() throws IOException
     {
         checkDocumentTypeExact("some.stringsdict", "ignore", ContentFileType.STRINGSDICT, 1f);
     }

     @Test
     public void identifiesByExtensionGETTEXT() throws IOException
     {
         checkDocumentTypeExact("some.po", "ignore", ContentFileType.GETTEXT, 1f);
         checkDocumentTypeExact("some.pot", "ignore", ContentFileType.GETTEXT, 1f);
     }

     @Test
     public void identifiesByExtensionRESX() throws IOException
     {
         checkDocumentTypeExact("some.resx", "ignore", ContentFileType.RESX, 1f);
         checkDocumentTypeExact("some.resw", "ignore", ContentFileType.RESX, 1f);
     }

     @Test
     public void identifiesByExtensionXLIFF() throws IOException
     {
         checkDocumentTypeExact("some.xlf", "ignore", ContentFileType.XLIFF, 1f);
     }

     @Test
     public void identifiesByExtensionYAML() throws IOException
     {
         checkDocumentTypeExact("some.yml", "ignore", ContentFileType.YAML, 1f);
         checkDocumentTypeExact("some.yaml", "ignore", ContentFileType.YAML, 1f);
     }

     @Test
     public void identifiesByExtensionJSON() throws IOException
     {
         checkDocumentTypeExact("some.json", "ignore", ContentFileType.JSON, 1f);
         checkDocumentTypeExact("some.js", "ignore", ContentFileType.JSON, 1f);
     }

     @Test
     public void identifiesByExtensionSVG() throws IOException
     {
         checkDocumentTypeExact("some.svg", "ignore", ContentFileType.XML, 1f);
     }

     @Test
     public void identifiesByExtensionHTML() throws IOException
     {
         checkDocumentTypeExact("some.html", "ignore", ContentFileType.HTML, 1f);
         checkDocumentTypeExact("some.htm", "ignore", ContentFileType.HTML, 1f);
     }

     @Test
     public void identifiesByExtensionDOCX() throws IOException
     {
         checkDocumentTypeExact("some.docx", "ignore", ContentFileType.DOCX, 1f);
     }

     @Test
     public void identifiesByExtensionPPTX() throws IOException
     {
         checkDocumentTypeExact("some.pptx", "ignore", ContentFileType.PPTX, 1f);
     }

     @Test
     public void identifiesByExtensionXLSX() throws IOException
     {
         checkDocumentTypeExact("some.xlsx", "ignore", ContentFileType.XLSX, 1f);
     }

     @Test
     public void identifiesByExtensionIDML() throws IOException
     {
         checkDocumentTypeExact("some.idml", "ignore", ContentFileType.IDML, 1f);
     }

     @Test
     public void identifiesByExtensionDOC() throws IOException
     {
         checkDocumentTypeExact("some.doc", "ignore", ContentFileType.DOC, 1f);
     }

     @Test
     public void identifiesByExtensionPPT() throws IOException
     {
         checkDocumentTypeExact("some.ppt", "ignore", ContentFileType.PPT, 1f);
     }

     @Test
     public void identifiesByExtensionXLS() throws IOException
     {
         checkDocumentTypeExact("some.xls", "ignore", ContentFileType.XLS, 1f);
     }

     @Test
     public void identifiesByExtensionQT() throws IOException
     {
         checkDocumentTypeExact("some.ts", "ignore", ContentFileType.QT, 1f);
     }

     @Test
     public void identifiesByExtensionCSV() throws IOException
     {
         checkDocumentTypeExact("some.csv", "ignore", ContentFileType.CSV, 1f);
     }

     @Test
     public void identifiesByExtensionTMX() throws IOException
     {
         checkDocumentTypeExact("some.tmx", "ignore", ContentFileType.TMX, 1f);
     }

     @Test
     public void identifiesByExtensionPRES() throws IOException
     {
         checkDocumentTypeExact("some.pres", "ignore", ContentFileType.PRES, 1f);
     }

     //</editor-fold>

     //<editor-fold desc="Tests: Identify by XML structure">

     @Test
     public void identifiesByXmlStructureXLIFF() throws Exception
     {
         String content = "<xliff version=\"2.0\"></xliff>";

         checkDocumentTypeExact(content, ContentFileType.XLIFF, 1f);
     }

     @Test
     public void identifiesByXmlStructureRESX() throws Exception
     {
         String content = "<root><resheader name=\"some\"><value>text</value></resheader></root>";

         checkDocumentTypeExact(content, ContentFileType.RESX, 1f);
     }

     @Test
     public void identifiesByXmlStructureTMX() throws Exception
     {
         String content = "<tmx version=\"1.\"></tmx>";

         checkDocumentTypeExact(content, ContentFileType.TMX, 1f);
     }

     @Test
     public void identifiesByXmlStructureANDROID() throws Exception
     {
         String content = "<resources></resources>";

         checkDocumentTypeExact(content, ContentFileType.ANDROID, 1f);
     }

     @Test
     public void identifiesByXmlStructureQT() throws Exception
     {
         String content = "<TS language=\"en\"></TS>";

         checkDocumentTypeExact(content, ContentFileType.QT, 1f);
     }

     @Test
     public void identifiesByXmlStructureIOSstringsdict() throws Exception
     {
         String content = "<plist><dict><key>some</key>"
                 + "<dict><key>NSStringLocalizedFormatKey</key><string>value</string></dict>"
                 + "</dict></plist>";

         checkDocumentTypeExact(content, ContentFileType.STRINGSDICT, 1f);
     }

     //</editor-fold>

     private void checkDocumentType(String contentString, ContentFileType fileType, float confidence) throws IOException
     {
         checkDocumentType("some file", contentString, fileType, confidence);
     }
     private void checkDocumentType(String fileName, String contentString, ContentFileType fileType, float confidence) throws IOException
     {
         try (InputStream data = new ByteArrayInputStream(contentString.getBytes("UTF-8")))
         {
             IdentifyResponse typeResponse = service.identify(fileName, data);

             assertThat(typeResponse.getFileName(), is(fileName));
             assertThat(typeResponse.getProbableTypes().size(), greaterThanOrEqualTo(1));
             assertThat(typeResponse.getProbableTypes().size(), is(typeResponse.getConfidence().size()));
             assertThat(typeResponse.getProbableTypes().get(0), is(fileType));
             assertThat(typeResponse.getConfidence().get(0), greaterThanOrEqualTo(confidence));
         }
     }

     //<editor-fold desc="Tests: Identify by text content">

     @Test
     public void identifiesByContentPLAINTEXT() throws Exception
     {
         String content = "some document";

         checkDocumentType(content, ContentFileType.PLAIN_TEXT, .2f);
     }

     @Test
     public void identifiesByContentJSON() throws Exception
     {
         String content = "{\"some text value\":\"some text here\",\"some green value\":\"some text there\"}\n";

         checkDocumentType(content, ContentFileType.JSON, .2f);
     }

     @Test
     public void identifiesByContentCSV() throws Exception
     {
         String content = "some,text,some,text";

         checkDocumentType(content, ContentFileType.CSV, .2f);
     }

     @Test
     public void identifiesByContentGETTEXT() throws Exception
     {
         String content = "msgid \"some text\"\n"
                 + "msgstr \"\"\n"
                 + "filesize.padding \"                           \"";

         checkDocumentType(content, ContentFileType.GETTEXT, .2f);
     }

     @Test
     public void identifiesByContentHTML() throws Exception
     {
         String content = "<div>some <p>text some</p> <span>text</span></div>";

         checkDocumentType(content, ContentFileType.HTML, .2f);
     }

     @Test
     public void identifiesByContentIOSstrings() throws Exception
     {
         String content = "\"some text value\" = \"some red green blue text\";\n";

         checkDocumentType(content, ContentFileType.IOS, .2f);
     }

     @Test
     public void identifiesByContentJavaProperties() throws Exception
     {
         String content = "some.value=some text";

         checkDocumentType(content, ContentFileType.JAVA_PROPERTIES, .2f);
     }

     @Test
     public void identifiesByContentYAML() throws Exception
     {
         String content = "some:\n"
                 + "  text: \"some text here\"\n";

         checkDocumentType(content, ContentFileType.YAML, .2f);
     }

     //</editor-fold>
}
