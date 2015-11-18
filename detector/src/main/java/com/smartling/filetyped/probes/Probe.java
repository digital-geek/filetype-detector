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
package com.smartling.filetyped.probes;

import com.smartling.filetyped.model.IdentifyResponse;
import java.io.IOException;
import java.util.Optional;

public interface Probe<T>
{
    /**
     * Returns Optional.empty if value is outside this detector's domain.
     * If value is in the domain, but could not be identified, returns IdentifyResponse.empty.
     * Otherwise, returns identified type and confidence values.
     */
    Optional<IdentifyResponse> detect(String fileName, T content) throws IOException;
}
