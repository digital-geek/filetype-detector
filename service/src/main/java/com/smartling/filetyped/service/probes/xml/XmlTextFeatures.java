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
import java.util.Optional;

public class XmlTextFeatures
{
    private final boolean hasTags;
    private final Optional<ContentFileType> detectedFileType;
    private final boolean hasSmartlingDirectives;

    public XmlTextFeatures(boolean hasTags, Optional<ContentFileType> detectedFileType, boolean hasSmartlingDirectives)
    {
        this.hasTags = hasTags;
        this.detectedFileType = detectedFileType;
        this.hasSmartlingDirectives = hasSmartlingDirectives;
    }

    public boolean hasTags()
    {
        return hasTags;
    }

    public Optional<ContentFileType> detectedFileType()
    {
        return detectedFileType;
    }

    public boolean hasSmartlingDirectives()
    {
        return hasSmartlingDirectives;
    }
}
