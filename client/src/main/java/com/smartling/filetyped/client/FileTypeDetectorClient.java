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
import com.smartling.filetyped.api.model.FileTypeResponseData;
import com.smartling.filetyped.api.model.ResponseStatus;
import com.smartling.filetyped.api.model.ResponseWrapper;
import com.smartling.filetyped.client.http.DefaultHttpClient;
import com.smartling.filetyped.client.http.HttpClient;
import com.smartling.filetyped.client.http.HttpClientException;
import com.smartling.filetyped.client.http.HttpResponse;
import com.smartling.filetyped.client.json.DefaultJsonParser;
import com.smartling.filetyped.client.json.JsonParser;
import java.io.InputStream;
import java.util.List;

public class FileTypeDetectorClient
{
    public static final String PRODUCTION_ENDPOINT = "https://api.smartling.com/v1/filetype";
    public static final String STAGING_ENDPOINT = "https://api.stg.smartling.net/v1/filetype";

    private static final String apiIdentify = "identify";

    private final HttpClient httpClient;
    private final JsonParser<FileTypeResponseData> jsonParser;

    public FileTypeDetectorClient()
    {
        this(STAGING_ENDPOINT);
    }

    public FileTypeDetectorClient(String apiBaseUrl)
    {
        this(new DefaultHttpClient(apiBaseUrl), new DefaultJsonParser());
    }

    FileTypeDetectorClient(HttpClient httpClient, JsonParser<FileTypeResponseData> jsonParser)
    {
        this.httpClient = httpClient;
        this.jsonParser = jsonParser;
    }

    public FileTypeResponse identify(String fileName, InputStream inputStream) throws HttpClientException
    {
        HttpResponse httpResponse = httpClient.upload(apiIdentify, fileName, inputStream);

        ResponseStatus<List<FileTypeResponse>> responseStatus = parseResponse(httpResponse);

        List<FileTypeResponse> fileResponse = responseStatus.getData();

        if (fileResponse == null || fileResponse.size() == 0)
        {
            return FileTypeResponse.emptyResponse(fileName);
        }

        return fileResponse.get(0);
    }

    private <T> ResponseStatus<T> parseResponse(HttpResponse httpResponse) throws HttpClientException
    {
        String jsonString = httpResponse.getContent();
        ResponseWrapper<T> responseWrapper;

        try
        {
            @SuppressWarnings("unchecked")
            ResponseWrapper<T> unchecked = (ResponseWrapper<T>)jsonParser.fromJson(jsonString);

            responseWrapper = unchecked;
        }
        catch (JsonSyntaxException e)
        {
            throw new HttpClientException(String.format("Invalid JSON: '%s'", safeSubstring(jsonString, 200)), e);
        }

        ResponseStatus<T> responseStatus = responseWrapper.getResponse();

        if (responseStatus == null)
        {
            throw new HttpClientException(httpResponse.getStatusCode(),
                    String.format("Unexpected server response: '%s'", safeSubstring(jsonString, 200)));
        }

        return responseStatus;
    }

    private String safeSubstring(String value, int size)
    {
        if (value != null)
            return value.substring(0, Math.min(value.length(), size));
        return null;
    }
}
