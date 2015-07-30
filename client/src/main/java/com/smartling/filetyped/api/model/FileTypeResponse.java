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
package com.smartling.filetyped.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FileTypeResponse
{
    @SerializedName("name")
    private final String fileName;

    @SerializedName("type")
    private final List<String> probableTypes;

    private final List<Float> confidence;

    public static FileTypeResponse emptyResponse(String fileName)
    {
        return new FileTypeResponse(fileName, Collections.<String>emptyList(), Collections.<Float>emptyList());
    }

    public FileTypeResponse(String fileName, List<String> probableTypes, List<Float> confidence)
    {
        this.fileName = fileName;
        this.probableTypes = probableTypes;
        this.confidence = confidence;
    }

    public String getFileName()
    {
        return fileName;
    }

    public List<String> getProbableTypes()
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
                .append("fileName", fileName)
                .append("probableTypes", probableTypes)
                .append("confidence", confidence)
                .toString();
    }
}
