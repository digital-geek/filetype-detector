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
package com.smartling.filetyped.service.probes;

import com.smartling.filetyped.dto.ContentFileType;
import com.smartling.filetyped.dto.ContentFileTypeExtensions;
import com.smartling.filetyped.dto.IdentifyResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

public class ExtensionBasedProbe implements Probe<InputStream>
{
    private final ContentFileTypeExtensions typeExtensions;

    public ExtensionBasedProbe()
    {
        this(new ContentFileTypeExtensions());
    }

    public ExtensionBasedProbe(ContentFileTypeExtensions typeExtensions)
    {
        this.typeExtensions = typeExtensions;
    }

    @Override
    public Optional<IdentifyResponse> detect(final String fileName, final InputStream content) throws IOException
    {
        Optional<ContentFileType> detectedType = detect(fileName);

        if (detectedType.isPresent())
        {
            IdentifyResponse response = new IdentifyResponse(fileName,
                    Collections.singletonList(detectedType.get()),
                    Collections.singletonList(1f)
            );
            return Optional.of(response);
        }

        return Optional.empty();
    }

    private Optional<ContentFileType> detect(String fileName)
    {
        Optional<ContentFileType> fileType = typeExtensions.get(fileName);

        if (!fileType.isPresent())
            return fileType;

        switch (fileType.get())
        {
            case PLAIN_TEXT:
                return Optional.empty();
        }
        return fileType;
    }
}
