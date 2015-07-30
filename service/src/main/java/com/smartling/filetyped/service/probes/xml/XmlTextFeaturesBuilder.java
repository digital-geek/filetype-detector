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

public class XmlTextFeaturesBuilder
{
    private boolean hasTags;
    private Optional<ContentFileType> fileType = Optional.empty();
    private boolean hasDirectives;

    public XmlTextFeaturesBuilder setHasTags(boolean hasTags)
    {
        this.hasTags = hasTags;
        return this;
    }

    public XmlTextFeaturesBuilder setDetectedFileType(ContentFileType fileType)
    {
        this.fileType = Optional.of(fileType);
        return this;
    }

    public XmlTextFeaturesBuilder setHasSmartlingDirectives(boolean hasDirectives)
    {
        this.hasDirectives = hasDirectives;
        return this;
    }

    public XmlTextFeatures build()
    {
        return new XmlTextFeatures(hasTags, fileType, hasDirectives);
    }
}
