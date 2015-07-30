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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MagicBytesTest
{
    private MagicBytes magicBytes;

    @Test
    public void extractsMagicBytesEmpty() throws Exception
    {
        assertExtractedMagic(new byte[]{}, is(new byte[]{}));
    }

    private void assertExtractedMagic(final byte[] contentBytes, final Matcher<byte[]> expectedBytes) throws IOException
    {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(contentBytes))
        {
            MagicBytes magicBytes = MagicBytes.of(byteStream);
            assertThat(magicBytes.getBytes(), expectedBytes);
        }
    }

    @Test
    public void extractsMagicBytesPartial() throws Exception
    {
        assertExtractedMagic(new byte[]{1, 2, 3, 4, 5}, is(new byte[]{1, 2, 3, 4, 5}));
    }

    @Test
    public void extractsMagicBytesFull() throws Exception
    {
        assertExtractedMagic(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}, is(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}));
    }

    @Test
    public void detectsNothingOfEmptyBuffer()
    {
        magicBytes = new MagicBytes(new byte[]{});

        assertFalse(magicBytes.isPKZIP());
        assertFalse(magicBytes.isMSOLE());
    }

    @Test
    public void detectsPKZIP()
    {
        magicBytes = new MagicBytes(new byte[]{0x50, 0x4b, 0x03, 0x04});

        assertTrue(magicBytes.isPKZIP());
    }

    @Test
    public void arrayStartsWithPrefix()
    {
        assertTrue(MagicBytes.startsWith(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3}));
        assertTrue(MagicBytes.startsWith(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}));
        assertTrue(MagicBytes.startsWith(new byte[]{}, new byte[]{}));
        assertFalse(MagicBytes.startsWith(new byte[]{1, 2, 3}, new byte[]{1, 2, 3, 4}));
        assertFalse(MagicBytes.startsWith(new byte[]{1, 2, 3}, new byte[]{1, 2, 3, 0}));
    }

    @Test
    public void detectsMSOLE()
    {
        byte[] signature = {
                (byte)0xd0, (byte)0xcf, 0x11, (byte)0xe0,
                (byte)0xa1, (byte)0xb1, 0x1a, (byte)0xe1
        };
        magicBytes = new MagicBytes(signature);

        assertTrue(magicBytes.isMSOLE());
    }

    @Test
    public void preservesStreamPosition() throws Exception
    {
        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        try (InputStream inputStream = new ByteArrayInputStream(bytes))
        {
            MagicBytes.of(inputStream);

            assertThat(inputStream.available(), is(bytes.length));
        }
    }
}
