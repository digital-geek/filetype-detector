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
package com.smartling.filetyped.client.http;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.smartling.filetyped.client.http.HttpClientExceptionMatcher.httpCodeEq;


public class DefaultHttpClientTest
{
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DefaultHttpClient client;

    @Before
    public void setUp()
    {
        client = new DefaultHttpClient(String.format("http://localhost:%d/api", wireMockRule.port()));
    }

    @Test
    public void invokesHttpPostToUrl() throws Exception
    {
        withHttpResponseData("/api/identify", "[{}]");

        clientIdentify();

        verify(postRequestedFor(urlEqualTo("/api/identify"))
            .withHeader("Content-Type", matching("multipart/form-data;.*")));
    }

    private HttpResponse clientIdentify() throws IOException
    {
        try (InputStream ignore = new ByteArrayInputStream("".getBytes()))
        {
            return client.upload("identify", "ignore", ignore);
        }
    }

    private void withHttpResponseData(String url, String data)
    {
        stubFor(post(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(String.format("{\"response\":{\"data\":%s,\"code\":\"SUCCESS\",\"messages\":[]}}", data))));
    }

    @Test
    public void throwsOnHttpError() throws Exception
    {
        withHttpResponseStatus("/api/identify", 500, "some error");

        thrown.expect(HttpClientException.class);
        thrown.expect(httpCodeEq(500));

        clientIdentify();
    }

    private void withHttpResponseStatus(String url, int statusCode, String message)
    {
        stubFor(post(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withBody(String.format("{\"response\":{\"data\":null,\"code\":\"SUCCESS\",\"messages\":[\"%s\"]}}", message))));
    }
}
