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
package com.smartling.filetyped.probes.text;

import com.smartling.filetyped.model.IdentifyResponse;
import com.smartling.ml.text.ContentPreprocessor;
import com.smartling.filetyped.probes.Probe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TextFamilyProbeChain implements Probe<InputStream>
{
    private List<Probe<String>> children;
    private final ContentPreprocessor preprocessor;

    public TextFamilyProbeChain(List<Probe<String>> children)
    {
        this(children, new ContentPreprocessor());
    }

    @SafeVarargs
    public TextFamilyProbeChain(Probe<String>... children)
    {
        this(Arrays.asList(children));
    }

    public TextFamilyProbeChain(List<Probe<String>> children, ContentPreprocessor preprocessor)
    {
        this.children = children;
        this.preprocessor = preprocessor;
    }

    @SafeVarargs
    public final void setChildren(Probe<String>... children)
    {
        this.children = Arrays.asList(children);
    }

    @Override
    public Optional<IdentifyResponse> detect(final String fileName, final InputStream content) throws IOException
    {
        Optional<String> convertedString = preprocessor.convert(content);

        if (!convertedString.isPresent())
            return Optional.empty();

        String contentString = convertedString.get();

        for (Probe<String> probe : children)
        {
            Optional<IdentifyResponse> response = probe.detect(fileName, contentString);

            if (response.isPresent())
                return response;
        }

        return Optional.empty();
    }
}
