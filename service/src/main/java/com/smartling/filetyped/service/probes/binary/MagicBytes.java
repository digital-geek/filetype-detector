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
package com.smartling.filetyped.service.probes.binary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reference: http://www.garykessler.net/library/file_sigs.html
 */
public class MagicBytes
{
    private static final int MAGIC_SIZE = 8;

    private static final byte[] PKZIP_MAGIC = new byte[]{0x50, 0x4b, 0x03, 0x04};
    private static final byte[] MSOLE_MAGIC = new byte[]{(byte)0xd0, (byte)0xcf, 0x11, (byte)0xe0,
                                                         (byte)0xa1, (byte)0xb1, 0x1a, (byte)0xe1};

    private final byte[] bytes;

    public static MagicBytes of(InputStream inputStream) throws IOException
    {
        inputStream.mark(MAGIC_SIZE);
        byte[] buffer = new byte[MAGIC_SIZE];

        int nread = inputStream.read(buffer);
        if (nread == -1)
            nread = 0;

        inputStream.reset();
        return new MagicBytes(Arrays.copyOf(buffer, nread));
    }

    public MagicBytes(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    public static boolean startsWith(final byte[] data, final byte[] prefix)
    {
        if (prefix.length > data.length)
            return false;
        return Arrays.equals(Arrays.copyOf(data, prefix.length), prefix);
    }

    public boolean isPKZIP()
    {
        return startsWith(bytes, PKZIP_MAGIC);
    }

    public boolean isMSOLE()
    {
        return startsWith(bytes, MSOLE_MAGIC);
    }
}
