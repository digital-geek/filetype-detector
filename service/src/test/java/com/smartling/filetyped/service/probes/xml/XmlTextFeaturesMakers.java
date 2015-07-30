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

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import com.smartling.filetyped.dto.ContentFileType;
import java.util.Optional;

public class XmlTextFeaturesMakers
{
    public static final Property<XmlTextFeatures, Boolean> hasTags = new Property<>();
    public static final Property<XmlTextFeatures, Optional<ContentFileType>> detectedFileType = new Property<>();
    public static final Property<XmlTextFeatures, Boolean> hasSmartlingDirectives = new Property<>();

    public static final Instantiator<XmlTextFeatures> XmlTextFeatures = lookup -> new XmlTextFeatures(
            lookup.valueOf(hasTags, false),
            lookup.valueOf(detectedFileType, Optional.empty()),
            lookup.valueOf(hasSmartlingDirectives, false)
    );
}
