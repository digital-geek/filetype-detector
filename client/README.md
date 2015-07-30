
Smartling File Type Detector Client
===================================


Using the client
----------------

```java
void doIdentify(String fileName, InputStream contentStream) {
    FileTypeDetectorClient client = new FileTypeDetectorClient();
    
    FileTypeResponse typeResponse = client.identify(fileName, contentStream);
    
    if (typeResponse.getProbableTypes().isEmpty()) {
        System.out.println(String.format("Could not identify file '%s'", typeResponse.getFileName()));
    }
    else {
        System.out.println(String.format("File '%s' identified as %s (%.1f confidence)",
            typeResponse.getFileName(),
            typeResponse.getProbableTypes().get(0),
            typeResponse.getConfidence().get(0) * 100
            ));
    }
}

```


Copyright and license
---------------------

Copyright 2015 Smartling, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
