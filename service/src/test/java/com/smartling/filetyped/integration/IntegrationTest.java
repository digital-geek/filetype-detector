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
package com.smartling.filetyped.integration;

import com.smartling.filetyped.SmartlingServiceApp;
import com.smartling.filetyped.api.model.FileTypeResponse;
import com.smartling.filetyped.client.FileTypeDetectorClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartlingServiceApp.class)
@WebAppConfiguration
@org.springframework.boot.test.IntegrationTest("server.port=0")
public class IntegrationTest
{
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    @Value("${local.server.port}")
    private int servicePort;

    private FileTypeDetectorClient client;

    @Before
    public void setUp()
    {
        client = new FileTypeDetectorClient(String.format("http://localhost:%d/v1/filetype", servicePort));
    }

    private void checkDocumentTypeExact(String contentString, String typeString, float confidence) throws IOException
    {
        checkDocumentTypeExact("some file", contentString, typeString, confidence);
    }
    private void checkDocumentTypeExact(String fileName, String contentString, String typeString, float confidence) throws IOException
    {
        try (InputStream data = new ByteArrayInputStream(contentString.getBytes("UTF-8")))
        {
            FileTypeResponse typeResponse = client.identify(fileName, data);

            assertThat(typeResponse.getFileName(), is(fileName));
            assertThat(typeResponse.getProbableTypes(), is(Arrays.asList(typeString)));
            assertThat(typeResponse.getProbableTypes().size(), is(typeResponse.getConfidence().size()));
            assertThat(typeResponse.getConfidence(), is(Arrays.asList(confidence)));
        }
    }

    //<editor-fold desc="Tests: Identify by extension">

    @Test
    public void identifiesByExtensionJavaProperties() throws IOException
    {
        checkDocumentTypeExact("some.properties", "ignore", "javaProperties", 1f);
    }

    @Test
    public void identifiesByExtensionIOSstrings() throws IOException
    {
        checkDocumentTypeExact("some.strings", "ignore", "ios", 1f);
    }

    @Test
    public void identifiesByExtensionIOSstringsdict() throws IOException
    {
        checkDocumentTypeExact("some.stringsdict", "ignore", "stringsdict", 1f);
    }

    @Test
    public void identifiesByExtensionGETTEXT() throws IOException
    {
        checkDocumentTypeExact("some.po", "ignore", "gettext", 1f);
        checkDocumentTypeExact("some.pot", "ignore", "gettext", 1f);
    }

    @Test
    public void identifiesByExtensionRESX() throws IOException
    {
        checkDocumentTypeExact("some.resx", "ignore", "resx", 1f);
        checkDocumentTypeExact("some.resw", "ignore", "resx", 1f);
    }

    @Test
    public void identifiesByExtensionXLIFF() throws IOException
    {
        checkDocumentTypeExact("some.xlf", "ignore", "xliff", 1f);
    }

    @Test
    public void identifiesByExtensionYAML() throws IOException
    {
        checkDocumentTypeExact("some.yml", "ignore", "yaml", 1f);
        checkDocumentTypeExact("some.yaml", "ignore", "yaml", 1f);
    }

    @Test
    public void identifiesByExtensionJSON() throws IOException
    {
        checkDocumentTypeExact("some.json", "ignore", "json", 1f);
        checkDocumentTypeExact("some.js", "ignore", "json", 1f);
    }

    @Test
    public void identifiesByExtensionSVG() throws IOException
    {
        checkDocumentTypeExact("some.svg", "ignore", "xml", 1f);
    }

    @Test
    public void identifiesByExtensionHTML() throws IOException
    {
        checkDocumentTypeExact("some.html", "ignore", "html", 1f);
        checkDocumentTypeExact("some.htm", "ignore", "html", 1f);
    }

    @Test
    public void identifiesByExtensionDOCX() throws IOException
    {
        checkDocumentTypeExact("some.docx", "ignore", "docx", 1f);
    }

    @Test
    public void identifiesByExtensionPPTX() throws IOException
    {
        checkDocumentTypeExact("some.pptx", "ignore", "pptx", 1f);
    }

    @Test
    public void identifiesByExtensionXLSX() throws IOException
    {
        checkDocumentTypeExact("some.xlsx", "ignore", "xlsx", 1f);
    }

    @Test
    public void identifiesByExtensionIDML() throws IOException
    {
        checkDocumentTypeExact("some.idml", "ignore", "idml", 1f);
    }

    @Test
    public void identifiesByExtensionDOC() throws IOException
    {
        checkDocumentTypeExact("some.doc", "ignore", "doc", 1f);
    }

    @Test
    public void identifiesByExtensionPPT() throws IOException
    {
        checkDocumentTypeExact("some.ppt", "ignore", "ppt", 1f);
    }

    @Test
    public void identifiesByExtensionXLS() throws IOException
    {
        checkDocumentTypeExact("some.xls", "ignore", "xls", 1f);
    }

    @Test
    public void identifiesByExtensionQT() throws IOException
    {
        checkDocumentTypeExact("some.ts", "ignore", "qt", 1f);
    }

    @Test
    public void identifiesByExtensionCSV() throws IOException
    {
        checkDocumentTypeExact("some.csv", "ignore", "csv", 1f);
    }

    @Test
    public void identifiesByExtensionTMX() throws IOException
    {
        checkDocumentTypeExact("some.tmx", "ignore", "tmx", 1f);
    }

    @Test
    public void identifiesByExtensionPRES() throws IOException
    {
        checkDocumentTypeExact("some.pres", "ignore", "pres", 1f);
    }

    //</editor-fold>

    //<editor-fold desc="Tests: Identify by XML structure">

    @Test
    public void identifiesByXmlStructureXLIFF() throws Exception
    {
        String content = "<xliff version=\"2.0\"></xliff>";

        checkDocumentTypeExact(content, "xliff", 1f);
    }

    @Test
    public void identifiesByXmlStructureRESX() throws Exception
    {
        String content = "<root><resheader name=\"some\"><value>text</value></resheader></root>";

        checkDocumentTypeExact(content, "resx", 1f);
    }

    @Test
    public void identifiesByXmlStructureTMX() throws Exception
    {
        String content = "<tmx version=\"1.\"></tmx>";

        checkDocumentTypeExact(content, "tmx", 1f);
    }

    @Test
    public void identifiesByXmlStructureANDROID() throws Exception
    {
        String content = "<resources></resources>";

        checkDocumentTypeExact(content, "android", 1f);
    }

    @Test
    public void identifiesByXmlStructureQT() throws Exception
    {
        String content = "<TS language=\"en\"></TS>";

        checkDocumentTypeExact(content, "qt", 1f);
    }

    @Test
    public void identifiesByXmlStructureIOSstringsdict() throws Exception
    {
        String content = "<plist><dict><key>some</key>"
                + "<dict><key>NSStringLocalizedFormatKey</key><string>value</string></dict>"
                + "</dict></plist>";

        checkDocumentTypeExact(content, "stringsdict", 1f);
    }

    //</editor-fold>

    private void checkDocumentType(String contentString, String typeString, float confidence) throws IOException
    {
        checkDocumentType("some file", contentString, typeString, confidence);
    }
    private void checkDocumentType(String fileName, String contentString, String typeString, float confidence) throws IOException
    {
        try (InputStream data = new ByteArrayInputStream(contentString.getBytes("UTF-8")))
        {
            FileTypeResponse typeResponse = client.identify(fileName, data);

            assertThat(typeResponse.getFileName(), is(fileName));
            assertThat(typeResponse.getProbableTypes().size(), greaterThanOrEqualTo(1));
            assertThat(typeResponse.getProbableTypes().size(), is(typeResponse.getConfidence().size()));
            assertThat(typeResponse.getProbableTypes().get(0), is(typeString));
            assertThat(typeResponse.getConfidence().get(0), greaterThanOrEqualTo(confidence));
        }
    }

    //<editor-fold desc="Tests: Identify by text content">

    @Test
    public void identifiesByContentPLAINTEXT() throws Exception
    {
        String content = "some document";

        checkDocumentType(content, "plainText", .2f);
    }

    @Test
    public void identifiesByContentJSON() throws Exception
    {
        String content = "{\"some text value\":\"some text here\",\"some green value\":\"some text there\"}\n";

        checkDocumentType(content, "json", .2f);
    }

    @Test
    public void identifiesByContentCSV() throws Exception
    {
        String content = "some,text,some,text";

        checkDocumentType(content, "csv", .2f);
    }

    @Test
    public void identifiesByContentGETTEXT() throws Exception
    {
        String content = "msgid \"some text\"\n"
                + "msgstr \"\"\n"
                + "filesize.padding \"                           \"";

        checkDocumentType(content, "gettext", .2f);
    }

    @Test
    public void identifiesByContentHTML() throws Exception
    {
        String content = "<div>some <p>text some</p> <span>text</span></div>";

        checkDocumentType(content, "html", .2f);
    }

    @Test
    public void identifiesByContentIOSstrings() throws Exception
    {
        String content = "\"some text value\" = \"some red green blue text\";\n";

        checkDocumentType(content, "ios", .2f);
    }

    @Test
    public void identifiesByContentJavaProperties() throws Exception
    {
        String content = "some.value=some text";

        checkDocumentType(content, "javaProperties", .2f);
    }

    @Test
    public void identifiesByContentYAML() throws Exception
    {
        String content = "some:\n"
                + "  text: \"some text here\"\n";

        checkDocumentType(content, "yaml", .2f);
    }

    //</editor-fold>
}
