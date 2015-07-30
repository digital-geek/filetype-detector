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
package com.smartling.filetyped.service.probes.binary;

import com.smartling.filetyped.dto.IdentifyResponse;
import com.smartling.filetyped.service.probes.Probe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class MicrosoftOleProbe implements Probe<InputStream>
{
    public Optional<IdentifyResponse> detect(String fileName, InputStream content) throws IOException
    {
        MagicBytes magicBytes = MagicBytes.of(content);

        if (magicBytes.isMSOLE())
        {
            // TODO: doc/ppt/xls
            return Optional.of(IdentifyResponse.empty(fileName));
        }

        return Optional.empty();
    }

}
