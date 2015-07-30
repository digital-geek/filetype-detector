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
package com.smartling.filetyped.client.json;

import com.google.gson.*;
import com.smartling.filetyped.api.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultJsonParserTest
{
    private DefaultJsonParser jsonParser;

    @Before
    public void setUp()
    {
        jsonParser = new DefaultJsonParser();
    }

    @Test
    public void parsesOkayResponse()
    {
        String json = responseWithData("null");

        ResponseWrapper responseWrapper = jsonParser.fromJson(json);
        ResponseStatus response = responseWrapper.getResponse();

        assertThat(response.getCode(), is(Status.SUCCESS.toString()));
        assertThat(response.getMessages(), is(Collections.emptyList()));
        assertThat(response.getData(), nullValue());
    }

    private String responseWithData(String data)
    {
        return String.format("{\"response\":{\"data\":%s,\"code\":\"SUCCESS\",\"messages\":[]}}", data);
    }

    @Test
    public void parsesErrorResponse()
    {
        String json = responseWithStatus(Status.AUTHENTICATION_ERROR, "some error");

        ResponseWrapper responseWrapper = jsonParser.fromJson(json);
        ResponseStatus response = responseWrapper.getResponse();

        assertThat(response.getCode(), is(Status.AUTHENTICATION_ERROR.toString()));
        assertThat(response.getMessages(), is(Arrays.asList("some error")));
        assertThat(response.getData(), nullValue());
    }

    private String responseWithStatus(Status status, String message)
    {
        return String.format("{\"response\":{\"data\":null,\"code\":\"%s\",\"messages\":[\"%s\"]}}", status, message);
    }

    @Test
    public void parsesDataObject()
    {
        String json = responseWithFileType("some file", probableTypes("some type"), confidence(1f));

        FileTypeResponse typeResponse = parseSingleResponse(json);

        assertThat(typeResponse.getFileName(), is("some file"));
        assertThat(typeResponse.getProbableTypes(), is(Arrays.asList("some type")));
        assertThat(typeResponse.getConfidence(), is(Arrays.asList(1f)));
    }

    private FileTypeResponse parseSingleResponse(String json)
    {
        FileTypeResponseData responseWrapper = jsonParser.fromJson(json);
        List<FileTypeResponse> data = responseWrapper.getResponse().getData();

        assertThat(data.size(), is(1));
        return data.get(0);
    }

    private List<String> probableTypes(String... types)
    {
        return Arrays.asList(types);
    }
    private List<Float> confidence(Float... values)
    {
        return Arrays.asList(values);
    }

    private String responseWithFileType(String fileName, List<String> probableTypes, List<Float> confidence)
    {
        JsonArray typeJson = new JsonArray();
        for (String s : probableTypes)
        {
            typeJson.add(new JsonPrimitive(s));
        }

        JsonArray confidenceJson = new JsonArray();
        for (Float x : confidence)
        {
            confidenceJson.add(new JsonPrimitive(x));
        }

        JsonObject fileType = new JsonObject();
        fileType.addProperty("name", fileName);
        fileType.add("type", typeJson);
        fileType.add("confidence", confidenceJson);

        JsonArray responseTypes = new JsonArray();
        responseTypes.add(fileType);

        return responseWithData(responseTypes.toString());
    }

    @Test
    public void preservesTypeOrder()
    {
        String json = responseWithFileType("ignore", probableTypes("red", "green", "blue"), confidence(0.2f, 0.1f, 0.4f));

        FileTypeResponse typeResponse = parseSingleResponse(json);

        assertThat(typeResponse.getProbableTypes(), is(Arrays.asList("red", "green", "blue")));
        assertThat(typeResponse.getConfidence(), is(Arrays.asList(0.2f, 0.1f, 0.4f)));
    }
}
