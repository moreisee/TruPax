/*
Copyright 2010-2013 CODERSLAGOON

This file is part of TruPax.

TruPax is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

TruPax is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
TruPax. If not, see http://www.gnu.org/licenses/.
*/

package coderslagoon.tclib.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import coderslagoon.baselib.util.BinUtils;
import coderslagoon.baselib.util.BytePtr;
import coderslagoon.tclib.container.Header;
import coderslagoon.tclib.crypto.AES256;
import coderslagoon.tclib.crypto.RIPEMD160;
import coderslagoon.tclib.crypto.Rand;
import coderslagoon.tclib.util.Key;
import coderslagoon.tclib.util.TCLibException;


public class HeaderTest {

    @Test
    public void test5GB_AES_RIPEMD160() throws IOException {

        // (block data taken from a real 5GB volume)
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream ins = getClass().getResourceAsStream("resources/5gb_aes_ripemd160_header");
        for (int b = ins.read(); b != -1; b = ins.read()) {
            bos.write(b);
        }
        ins.close();

        byte[] hdata = bos.toByteArray();
        byte[] hdata_orig = hdata.clone();

        assertTrue(Header.SIZE == hdata.length);

        try {
            Header hdr = new Header(new Key.ByteArray(
                    "test12345".getBytes()), hdata, 0);

            assertTrue(4      == hdr.version.value);
            assertTrue(0x0600 == hdr.minimumVersion.value);
            assertEquals("4.0", hdr.version.toString());
            assertTrue(hdr.minimumVersion.compatible(Header.Version.LOWEST_APP));
            assertTrue(hdr.version.compatible(Header.Version.LOWEST_HEADER));
            assertFalse(hdr.version.compatible(new Header.Version(5)));

            final long TC_5GB = 5L * 1024L * 1024L * 1024L - Header.SIZE * 2;

            assertTrue(TC_5GB == hdr.dataAreaSize);
            assertTrue(TC_5GB == hdr.sizeofVolume);

            assertTrue(Header.SIZE == hdr.dataAreaOffset);

            // NOTE: reserved3 is pure random so we can't really check it
            for (BytePtr bp : new BytePtr[] { hdr.reserved,
                                              hdr.reserved2 }) {
                for (int i = 0; i < bp.len; i++) {
                    assertTrue(0 == bp.at(i));
                }
            }

            assertTrue(hdr.blockCipher .equals(AES256   .class));
            assertTrue(hdr.hashFunction.equals(RIPEMD160.class));

            byte[] enc = hdr.encode("test12345".getBytes());
            assertTrue(Header.SIZE == enc.length);
            assertTrue(BinUtils.arraysEquals(enc, hdata_orig));
            assertTrue(hdr.toString().contains("volume-size"));

            hdr.reserved  =
            hdr.reserved2 =
            hdr.reserved3 =
            hdr.hiddenVolumeHeader = null;
            enc = hdr.encode("test12345".getBytes());
            assertTrue(Header.SIZE == enc.length);
            assertFalse(BinUtils.arraysEquals(enc, hdata_orig));

            hdr.erase();
        }
        catch (TCLibException tle) {
            tle.printStackTrace(System.err);
            System.err.print(tle.getMessage());
            fail();
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    static class DummyRand extends Rand {
        long bytesTotal;
        int counter;
        public void make(byte[] buf, int ofs, int len) {
            for (int end = ofs + len; ofs < end; ofs++) {
                buf[ofs] = (byte)this.counter++;
            }
            this.bytesTotal += len;
        }
        public void test() throws Throwable { }
        public void erase() { }
    }

    @Test
    public void testKeyMaking() throws Exception {
        Header hdr = new Header(RIPEMD160.class, AES256.class);

        hdr.generateKeyMaterial(null);

        int keysSize = AES256.class.newInstance().keySize() << 1;

        int sum = 0;  // very, very unlikely that the whole key will be all zero
        for (int i = 0; i < keysSize; i++) {
            sum += hdr.keyMaterial.at(i) & 0xff;
        }
        assertTrue(0 < sum);
        for (int i = keysSize; i < Header.KEY_MATERIAL_SIZE; i++) {
            assertTrue(0 == hdr.keyMaterial.at(i));
        }

        DummyRand drnd = new DummyRand();

        hdr.generateKeyMaterial(drnd);

        assertTrue(drnd.bytesTotal == keysSize);

        for (int i = 0; i < keysSize; i++) {
            assertTrue((byte)i == hdr.keyMaterial.at(i));
        }
        for (int i = keysSize; i < Header.KEY_MATERIAL_SIZE; i++) {
            assertTrue(0 == hdr.keyMaterial.at(i));
        }
    }
}
