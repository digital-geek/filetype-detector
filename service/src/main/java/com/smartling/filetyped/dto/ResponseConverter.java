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

import com.smartling.ml.ClassifierResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResponseConverter
{
    public IdentifyResponse convert(String fileName, ClassifierResult classifierResult)
    {
        return new IdentifyResponse(
                fileName,
                fileTypes(classifierResult.getLabels()),
                floats(classifierResult.getConfidence())
        );
    }

    private static List<ContentFileType> fileTypes(final String[] labels)
    {
        return Arrays
                .stream(labels)
                .map(ContentFileType::getByName)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static List<Float> floats(final double[] values)
    {
        return Arrays
                .stream(values)
                .mapToObj(x -> (float)x)
                .collect(Collectors.toList());
    }
}
