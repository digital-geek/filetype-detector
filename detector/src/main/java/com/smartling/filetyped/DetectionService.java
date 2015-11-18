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

import com.smartling.filetyped.model.IdentifyResponse;
import com.smartling.filetyped.probes.ExtensionBasedProbe;
import com.smartling.filetyped.probes.Probe;
import com.smartling.filetyped.probes.ProbeChain;
import com.smartling.filetyped.probes.binary.MicrosoftOleProbe;
import com.smartling.filetyped.probes.binary.ZipContainerProbe;
import com.smartling.filetyped.probes.text.TextClassifierProbe;
import com.smartling.filetyped.probes.text.TextFamilyProbeChain;
import com.smartling.filetyped.probes.xml.XmlClassifierProbe;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class DetectionService
{
    private final Probe<InputStream> probe;
    private final RandomString randomString;

    public DetectionService()
    {
        this(probeGraph(), new RandomString());
    }

    public DetectionService(Probe<InputStream> probe, RandomString randomString)
    {
        this.probe = probe;
        this.randomString = randomString;
    }

    private static Probe<InputStream> probeGraph()
    {
        return ProbeChain.of(
                new ExtensionBasedProbe(),
                new ZipContainerProbe(),
                new MicrosoftOleProbe(),
                new TextFamilyProbeChain(
                        new XmlClassifierProbe(),
                        new TextClassifierProbe()
                )
        );
    }

    public IdentifyResponse identify(String fileName, InputStream inputStream) throws IOException
    {
        String referenceId = randomString.random(8);

        try (InputStream bufferedStream = new BufferedInputStream(inputStream))
        {
            Optional<IdentifyResponse> optionalResponse = probe.detect(fileName, bufferedStream);

            if (optionalResponse.isPresent())
            {
                IdentifyResponse response = optionalResponse.get();
                response.setReferenceId(referenceId);
                return response;
            }
        }

        return IdentifyResponse.empty(referenceId, fileName);
    }
}