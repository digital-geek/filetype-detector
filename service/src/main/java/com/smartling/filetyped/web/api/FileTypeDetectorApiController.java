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
package com.smartling.filetyped.web.api;

import com.smartling.filetyped.dto.ApiResponse;
import com.smartling.filetyped.dto.ApiStatus;
import com.smartling.filetyped.dto.IdentifyResponse;
import com.smartling.filetyped.dto.IdentifyResponseData;
import com.smartling.filetyped.dto.ResponseStatus;
import com.smartling.filetyped.service.DetectionService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/v1/filetype")
public class FileTypeDetectorApiController
{
    private static Logger logger = LoggerFactory.getLogger(FileTypeDetectorApiController.class);

    @Autowired
    private DetectionService detectionService;

    @RequestMapping(value = "/identify", method = RequestMethod.POST)
    public ApiResponse handleIdentifyRequest(MultipartHttpServletRequest request)
    {
        IdentifyResponseData data = processUploads(request);

        ResponseStatus<IdentifyResponseData> responseStatus = new ResponseStatus<>(ApiStatus.SUCCESS);
        responseStatus.setData(data);
        return new ApiResponse<>(responseStatus);
    }

    private IdentifyResponseData processUploads(MultipartHttpServletRequest request)
    {
        List<IdentifyResponse> identified = stream(request.getFileNames())
                .flatMap(name -> request.getFiles(name).stream())
                .map(this::identifyFile)
                .collect(Collectors.toList());

        return new IdentifyResponseData(identified);
    }

    private static <T> Stream<T> stream(Iterator<T> iterator)
    {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private IdentifyResponse identifyFile(MultipartFile file)
    {
        logger.info("Identify requested: ({} B) '{}'", file.getSize(), file.getOriginalFilename());

        String fileName = file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream())
        {
            return detectionService.identify(fileName, inputStream);
        }
        catch (IOException e)
        {
            logger.error(String.format("Failed to read uploaded file '%s", fileName), e);
            return IdentifyResponse.empty(fileName);
        }
    }
}
