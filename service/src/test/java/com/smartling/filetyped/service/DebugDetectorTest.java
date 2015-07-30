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
package com.smartling.filetyped.service;

import com.smartling.filetyped.dto.IdentifyResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class DebugDetectorTest
{
    private static final Logger logger = LoggerFactory.getLogger(DebugDetectorTest.class);

    @Test
    public void runTest() throws IOException
    {
        String fileName = "...";

        File file = new File(fileName);
        try (InputStream inputStream = FileUtils.openInputStream(file))
        {
            DetectionServiceImpl detector = new DetectionServiceImpl();
            IdentifyResponse response = detector.identify(file.getName(), inputStream);
            logger.info("{}", response);
        }
    }
}
