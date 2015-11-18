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

import java.util.Optional;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class OptionalIdentifyResponseMatcher extends TypeSafeMatcher<Optional<IdentifyResponse>>
{
    private final Optional<IdentifyResponse> other;

    public static OptionalIdentifyResponseMatcher optionalEq(Optional<IdentifyResponse> other)
    {
        return new OptionalIdentifyResponseMatcher(other);
    }

    public OptionalIdentifyResponseMatcher(Optional<IdentifyResponse> other)
    {
        this.other = other;
    }

    @Override
    protected boolean matchesSafely(final Optional<IdentifyResponse> item)
    {
        return (other.isPresent() == item.isPresent())
                && (!item.isPresent() || IdentifyResponseMatcher.eq(other.get()).matchesSafely(item.get()));
    }

    @Override
    public void describeTo(final Description description)
    {
        if (!other.isPresent())
        {
            description.appendText("optional.empty");
        }
        else
        {
            IdentifyResponseMatcher.eq(other.get()).describeTo(description);
        }
    }

    @Override
    protected void describeMismatchSafely(Optional<IdentifyResponse> item, Description mismatchDescription)
    {
        if (!item.isPresent())
        {
            mismatchDescription.appendText("was optional.empty");
        }
        else
        {
            IdentifyResponseMatcher.eq(null).describeMismatchSafely(item.get(), mismatchDescription);
        }
    }
}
