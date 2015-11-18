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
package com.smartling.filetyped.model;

import com.smartling.ml.ClassifierResult;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResponseConverterTest
{
    private ResponseConverter converter = new ResponseConverter();

    @Test
    public void convertsEmptyResult()
    {
        IdentifyResponse response = converter.convert("some file", ClassifierResult.empty());

        assertThat(response.getFileName(), is("some file"));
        assertThat(response.getProbableTypes(), is(Collections.emptyList()));
        assertThat(response.getConfidence(), is(Collections.emptyList()));
    }

    @Test
    public void convertsFullResult()
    {
        ClassifierResult classifierResult = new ClassifierResult(
                new String[]{"javaProperties", "ios", "csv", "plainText"},
                new double[]{0.8, 1, 18, 0.4}
        );
        IdentifyResponse response = converter.convert("some file", classifierResult);

        assertThat(response.getFileName(), is("some file"));
        assertThat(response.getProbableTypes(), is(Arrays.asList(
                                ContentFileType.JAVA_PROPERTIES,
                                ContentFileType.IOS,
                                ContentFileType.CSV,
                                ContentFileType.PLAIN_TEXT
                        )
                )
        );
        assertThat(response.getConfidence(), is(Arrays.asList(.8f, 1f, 18f, .4f)));
    }
}
