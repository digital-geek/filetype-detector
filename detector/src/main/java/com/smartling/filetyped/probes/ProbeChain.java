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
package com.smartling.filetyped.probes;

import com.smartling.filetyped.model.IdentifyResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProbeChain<T> implements Probe<T>
{
    private final List<Probe<T>> children;

    public ProbeChain(List<Probe<T>> children)
    {
        this.children = children;
    }

    @SafeVarargs
    public static <T> ProbeChain<T> of(Probe<T>... children)
    {
        return new ProbeChain<T>(Arrays.asList(children));
    }

    public Optional<IdentifyResponse> detect(final String fileName, final T content) throws IOException
    {
        for (Probe<T> probe : children)
        {
            Optional<IdentifyResponse> response = probe.detect(fileName, content);

            if (response.isPresent())
                return response;
        }

        return Optional.empty();
    }
}
