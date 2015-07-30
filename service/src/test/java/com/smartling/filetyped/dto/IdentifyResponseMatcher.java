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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IdentifyResponseMatcher extends TypeSafeMatcher<IdentifyResponse>
{
    private final IdentifyResponse other;

    public static IdentifyResponseMatcher eq(IdentifyResponse other)
    {
        return new IdentifyResponseMatcher(other);
    }

    public IdentifyResponseMatcher(IdentifyResponse other)
    {
        this.other = other;
    }

    @Override
    protected boolean matchesSafely(final IdentifyResponse item)
    {
        return other.getFileName().equals(item.getFileName())
                && other.getProbableTypes().equals(item.getProbableTypes())
                && other.getConfidence().equals(item.getConfidence());
    }

    @Override
    public void describeTo(final Description description)
    {
        description.appendText("fileName ")
                   .appendValue(other.getFileName());
        description.appendText(" probableTypes ")
                   .appendValue(other.getProbableTypes());
        description.appendText(" confidence ")
                   .appendValue(other.getConfidence());
    }

    @Override
    protected void describeMismatchSafely(IdentifyResponse item, Description mismatchDescription)
    {
        mismatchDescription.appendText("was fileName ")
                   .appendValue(item.getFileName());
        mismatchDescription.appendText(" probableTypes ")
                   .appendValue(item.getProbableTypes());
        mismatchDescription.appendText(" confidence ")
                   .appendValue(item.getConfidence());
    }
}
