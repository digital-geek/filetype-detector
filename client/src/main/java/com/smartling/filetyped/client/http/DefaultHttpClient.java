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

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DefaultHttpClient implements HttpClient
{
    private final String baseApiUrl;

    public DefaultHttpClient(String baseApiUrl)
    {
        this.baseApiUrl = baseApiUrl;
    }

    @Override
    public HttpResponse upload(String urlPath, String fileName, InputStream inputStream) throws HttpClientException
    {
        try (CloseableHttpClient httpClient = HttpClients.createDefault())
        {
            URI uri = concatUri(baseApiUrl, urlPath);

            HttpPost httpPost = new HttpPost(uri);
            InputStreamBody fileBody = new InputStreamBody(inputStream, fileName);

            HttpEntity request = MultipartEntityBuilder
                    .create()
                    .addPart("file", fileBody)
                    .build();

            httpPost.setEntity(request);

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost))
            {
                checkStatusCodeOK(httpResponse.getStatusLine());

                return convertHttpResponse(httpResponse);
            }
        }
        catch (HttpClientException e)
        {
            throw e;
        }
        catch (URISyntaxException|IOException e)
        {
            throw new HttpClientException(e);
        }
    }

    private URI concatUri(String baseApiUrl, String path) throws MalformedURLException, URISyntaxException
    {
        return new URL(String.format("%s/%s", baseApiUrl, path)).toURI();
    }

    private void checkStatusCodeOK(StatusLine statusLine) throws HttpClientException
    {
        int statusCode = statusLine.getStatusCode();

        if (statusCode != 200)
        {
            throw new HttpClientException(statusCode, statusLine.toString());
        }
    }

    private HttpResponse convertHttpResponse(org.apache.http.HttpResponse response) throws IOException
    {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        String contentString = EntityUtils.toString(response.getEntity());
        return new HttpResponse(statusCode, contentString);
    }
}
