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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HttpClientExceptionMatcher extends TypeSafeMatcher<HttpClientException>
{
    private final int expectedCode;

    public static HttpClientExceptionMatcher httpCodeEq(int code)
    {
        return new HttpClientExceptionMatcher(code);
    }

    public HttpClientExceptionMatcher(int expectedCode)
    {
        this.expectedCode = expectedCode;
    }

    @Override
    protected boolean matchesSafely(HttpClientException item)
    {
        return item.getHttpCode() == expectedCode;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("httpCode ")
                .appendValue(expectedCode);
    }

    @Override
    protected void describeMismatchSafely(HttpClientException item, Description mismatchDescription)
    {
        mismatchDescription.appendText("was ")
                .appendValue(item.getHttpCode());
    }
}
