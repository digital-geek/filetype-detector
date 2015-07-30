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
package com.smartling.filetyped.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IdentifyResponse
{
    @JsonProperty(value = "name", index = 0)
    private final String fileName;

    @JsonProperty(value = "type", index = 1)
    private final List<ContentFileType> probableTypes;

    @JsonProperty(value = "confidence", index = 2)
    private final List<Float> confidence;

    @JsonProperty(value = "referenceId", index = 3)
    private String referenceId;

    public static IdentifyResponse empty(String fileName)
    {
        return empty(null, fileName);
    }

    public static IdentifyResponse empty(String referenceId, String fileName)
    {
        return new IdentifyResponse(referenceId, fileName, Collections.<ContentFileType>emptyList(), Collections.<Float>emptyList());
    }

    public IdentifyResponse(String fileName, List<ContentFileType> probableTypes, List<Float> confidence)
    {
        this(null, fileName, probableTypes, confidence);
    }

    public IdentifyResponse(String referenceId, String fileName, List<ContentFileType> probableTypes, List<Float> confidence)
    {
        this.referenceId = referenceId;
        this.fileName = fileName;
        this.probableTypes = probableTypes;
        this.confidence = confidence;
    }

    public String getFileName()
    {
        return fileName;
    }

    public List<ContentFileType> getProbableTypes()
    {
        return probableTypes;
    }

    public List<Float> getConfidence()
    {
        return confidence;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("referenceId", referenceId)
                .append("fileName", fileName)
                .append("probableTypes", probableTypes)
                .append("confidence", confidence)
                .toString();
    }

    public void setReferenceId(String referenceId)
    {
        this.referenceId = referenceId;
    }

    public String getReferenceId()
    {
        return referenceId;
    }
}
