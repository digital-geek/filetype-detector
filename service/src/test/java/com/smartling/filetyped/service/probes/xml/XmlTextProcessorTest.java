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
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XmlTextProcessorTest
{
    private static final String XML_DOCUMENT = "<?xml version=\"1.0\"?><root><children><child></child></children></root>";

    @Mock
    private TextSearchEngine searchEngine;

    @Mock
    private Map<String, Integer> searchResult;

    @InjectMocks
    private XmlTextProcessor processor;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        when(searchResult.get(any())).thenReturn(0);
        when(searchEngine.search(any())).thenReturn(searchResult);

        withSearchCount("<", 5);
        withSearchCount(">", 5);
    }

    @Test
    public void returnsDefaultStats()
    {
        XmlTextFeatures response = processor.process("ignore");

        assertFalse(response.hasTags());
        assertThat(response.detectedFileType(), is(Optional.empty()));
        assertFalse(response.hasSmartlingDirectives());
    }

    @Test
    public void setsHasTagsOnProperDocument() throws Exception
    {
        XmlTextFeatures response = processor.process(XML_DOCUMENT);

        assertTrue(response.hasTags());
    }

    @Test
    public void callsSearchEngineOnContent() throws Exception
    {
        processor.process("some text");

        verify(searchEngine).search("some text");
    }

    @Test
    public void detectsXLIFF() throws Exception
    {
        withSearchCount("<xliff ", 1);

        checkProbeDetects(ContentFileType.XLIFF);
        verify(searchEngine).addKeyword("<xliff ");
    }

    private void withSearchCount(String keyword, int count)
    {
        when(searchResult.get(keyword)).thenReturn(count);
    }

    private void checkProbeDetects(ContentFileType fileType) throws IOException
    {
        XmlTextFeatures response = processor.process("ignore");
        assertThat(response.detectedFileType(), is(Optional.of(fileType)));
    }

    @Test
    public void detectsRESX() throws Exception
    {
        withSearchCount("<resheader ", 1);

        checkProbeDetects(ContentFileType.RESX);
        verify(searchEngine).addKeyword("<resheader ");
    }

    @Test
    public void detectsTMX1() throws Exception
    {
        withSearchCount("<tmx ", 1);

        checkProbeDetects(ContentFileType.TMX);
        verify(searchEngine).addKeyword("<tmx ");
    }

    @Test
    public void detectsTMX2() throws Exception
    {
        withSearchCount("<tmx:", 1);

        checkProbeDetects(ContentFileType.TMX);
        verify(searchEngine).addKeyword("<tmx:");
    }

    @Test
    public void detectsANDROID() throws Exception
    {
        withSearchCount("<resources", 1);

        checkProbeDetects(ContentFileType.ANDROID);
        verify(searchEngine).addKeyword("<resources");
    }

    @Test
    public void detectsQT() throws Exception
    {
        withSearchCount("<TS ", 1);

        checkProbeDetects(ContentFileType.QT);
        verify(searchEngine).addKeyword("<TS ");
    }

    @Test
    public void detectsSTRINGSDICT() throws Exception
    {
        withSearchCount("NSStringLocalizedFormatKey", 1);

        checkProbeDetects(ContentFileType.STRINGSDICT);
        verify(searchEngine).addKeyword("NSStringLocalizedFormatKey");
    }

    @Test
    public void detectsPLISTasSTRINGSDICT() throws Exception
    {
        withSearchCount("<dict>", 1);

        checkProbeDetects(ContentFileType.STRINGSDICT);
        verify(searchEngine).addKeyword("<dict>");
    }

    @Test
    public void setsHasTagsWhenBelowThresholdLT() throws Exception
    {
        withSearchCount("<", 4);

        assertNoTags(XML_DOCUMENT);
        verify(searchEngine).addKeyword("<");
    }

    @Test
    public void setsHasTagsWhenBelowThresholdRT() throws Exception
    {
        withSearchCount(">", 4);

        assertNoTags(XML_DOCUMENT);
        verify(searchEngine).addKeyword(">");
    }

    @Test
    public void setsHasTagsWhenNotMarkup() throws Exception
    {
        assertHasTags("  <some>");
        assertNoTags("hello <some>");
        assertNoTags("<56some");
        assertNoTags("< some");
        assertNoTags("<-some");
        assertNoTags("<.some");
    }

    private void assertNoTags(final String document)
    {
        XmlTextFeatures response = processor.process(document);
        assertFalse(response.hasTags());
    }
    private void assertHasTags(final String document)
    {
        XmlTextFeatures response = processor.process(document);
        assertTrue(response.hasTags());
    }

    @Test
    public void setsHasSmartlingDirectives() throws Exception
    {
        assertHasSmartlingDirectives("smartling.translate_paths");
        assertHasSmartlingDirectives("smartling.TRANSLATE_PATHS");
    }

    private void assertHasSmartlingDirectives(final String document)
    {
        XmlTextFeatures response = processor.process(document);
        assertTrue(response.hasSmartlingDirectives());
    }
}
