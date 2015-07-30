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
package com.smartling.filetyped.client;

import com.google.gson.JsonSyntaxException;
import com.smartling.filetyped.api.model.FileTypeResponse;
import com.smartling.filetyped.api.model.ResponseStatus;
import com.smartling.filetyped.api.model.ResponseWrapper;
import com.smartling.filetyped.api.model.Status;
import com.smartling.filetyped.client.http.HttpClient;
import com.smartling.filetyped.client.http.HttpClientException;
import com.smartling.filetyped.client.http.HttpResponse;
import com.smartling.filetyped.client.json.JsonParser;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileTypeDetectorClientTest
{
    @Mock
    private HttpClient httpClient;

    private InputStream ignore;

    @Mock
    private JsonParser<ResponseWrapper> jsonParser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private FileTypeDetectorClient client;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        withHttpResponse(200, "set up code");
        withServerDetectedTypes(Collections.<String>emptyList(), Collections.<Float>emptyList());
    }

    @Test
    public void invokesHttpClientUpload() throws Exception
    {
        client.identify("some file", ignore);

        verify(httpClient).upload("identify", "some file", ignore);
    }

    @Test
    public void parsesServerResponseOK() throws Exception
    {
        withHttpResponse(200, "some json");

        client.identify("ignore", ignore);

        verify(jsonParser).fromJson("some json");
    }

    private void withHttpResponse(int statusCode, String content) throws HttpClientException
    {
        HttpResponse httpResponse = new HttpResponse(statusCode, content);
        when(httpClient.upload(any(), any(), any())).thenReturn(httpResponse);
    }

    @Test
    public void returnsParsedResponseOK() throws Exception
    {
        withServerDetectedTypes(Arrays.asList("some type"), Arrays.asList(0.8f));

        FileTypeResponse response = client.identify("ignore", ignore);

        assertThat(response.getProbableTypes(), is(Arrays.asList("some type")));
        assertThat(response.getConfidence(), is(Arrays.asList(0.8f)));
    }

    private void withServerDetectedTypes(List<String> probableTypes, List<Float> confidence)
    {
        withServerResponseData(Arrays.asList(new FileTypeResponse("ignore wsdt", probableTypes, confidence)));
    }

    private <T> void withServerResponseData(T data)
    {
        ResponseStatus<T> response = new ResponseStatus<>(Status.SUCCESS);
        response.setData(data);
        ResponseWrapper<T> responseWrapper = new ResponseWrapper<>(response);
        when(jsonParser.fromJson(anyString())).thenReturn(responseWrapper);
    }

    @Test
    public void returnsEmptyResponseOnResponseDataNull() throws Exception
    {
        withServerResponseData(null);

        FileTypeResponse response = client.identify("ignore", ignore);

        assertThat(response.getProbableTypes(), is(Collections.<String>emptyList()));
        assertThat(response.getConfidence(), is(Collections.<Float>emptyList()));
    }

    @Test
    public void returnsEmptyResponseOnResponseDataEmptyList() throws Exception
    {
        withServerResponseData(Collections.<FileTypeResponse>emptyList());

        FileTypeResponse response = client.identify("ignore", ignore);

        assertThat(response.getProbableTypes(), is(Collections.<String>emptyList()));
        assertThat(response.getConfidence(), is(Collections.<Float>emptyList()));
    }

    @Test
    public void throwsOnJsonError() throws Exception
    {
        withHttpResponse(200, "some json");
        when(jsonParser.fromJson(anyString()))
                .thenThrow(new JsonSyntaxException("some text"));

        thrown.expect(HttpClientException.class);
        thrown.expectMessage(containsString("some json"));

        client.identify("ignore", ignore);
    }

    @Test
    public void throwsOnUnexpectedResponse() throws Exception
    {
        withHttpResponse(200, "some json");
        ResponseWrapper responseWrapper = new ResponseWrapper<>(null);
        when(jsonParser.fromJson(anyString())).thenReturn(responseWrapper);

        thrown.expect(HttpClientException.class);
        thrown.expectMessage(containsString("some json"));

        client.identify("ignore", ignore);
    }
}
