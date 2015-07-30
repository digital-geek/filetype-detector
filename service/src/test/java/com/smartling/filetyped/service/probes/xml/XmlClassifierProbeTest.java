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

import com.natpryce.makeiteasy.Maker;
import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.filetyped.dto.IdentifyResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.smartling.filetyped.dto.OptionalIdentifyResponseMatcher.optionalEq;
import static com.smartling.filetyped.service.probes.xml.XmlTextFeaturesMakers.XmlTextFeatures;
import static com.smartling.filetyped.service.probes.xml.XmlTextFeaturesMakers.detectedFileType;
import static com.smartling.filetyped.service.probes.xml.XmlTextFeaturesMakers.hasSmartlingDirectives;
import static com.smartling.filetyped.service.probes.xml.XmlTextFeaturesMakers.hasTags;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XmlClassifierProbeTest
{
    @Mock
    private XmlTextProcessor textProcessor;

    @InjectMocks
    private XmlClassifierProbe probe;

    private Maker<XmlTextFeatures> defaultStats = a(XmlTextFeatures, with(hasTags, true));

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        when(textProcessor.process(any())).thenReturn(make(defaultStats));
    }

    @Test
    public void callsTextProcessorOnContent() throws IOException
    {
        probe.detect("ignore", "some text");

        verify(textProcessor).process("some text");
    }

    @Test
    public void returnsDetectedFileType() throws IOException
    {
        withDocumentDetectedType(ContentFileType.CSV);

        checkProbeReturns(ContentFileType.CSV, 1f);
    }

    private void withDocumentDetectedType(ContentFileType value)
    {
        XmlTextFeatures textStats = make(defaultStats.but(with(detectedFileType, Optional.of(value))));
        when(textProcessor.process(any())).thenReturn(textStats);
    }

    private void checkProbeReturns(ContentFileType fileType, float confidence) throws IOException
    {
        IdentifyResponse expectedResponse = new IdentifyResponse("some file",
                Collections.singletonList(fileType),
                Collections.singletonList(confidence));

        Optional<IdentifyResponse> response = probe.detect("some file", "ignore");

        assertThat(response, is(optionalEq(Optional.of(expectedResponse))));
    }

    @Test
    public void returnsNothingIfNotAMarkupFile() throws IOException
    {
        withDocumentHavingTags(false);

        Optional<IdentifyResponse> response = probe.detect("ignore", "ignore");

        assertThat(response, is(Optional.empty()));
    }

    private void withDocumentHavingTags(boolean value)
    {
        XmlTextFeatures textStats = make(defaultStats.but(with(hasTags, value)));
        when(textProcessor.process(any())).thenReturn(textStats);
    }

    @Test
    public void returnsXmlIfDirectivesFound() throws IOException
    {
        withDocumentHavingSmartlingDirectives(true);

        checkProbeReturns(ContentFileType.XML, 1f);
    }

    private void withDocumentHavingSmartlingDirectives(boolean value)
    {
        XmlTextFeatures textStats = make(defaultStats.but(with(hasSmartlingDirectives, value)));
        when(textProcessor.process(any())).thenReturn(textStats);
    }

    @Test
    public void returnsHtmlIfHasTagsAndNotXml() throws IOException
    {
        checkProbeReturns(ContentFileType.HTML, 1f);
    }
}
