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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import coderslagoon.baselib.util.BinUtils;
import coderslagoon.tclib.container.Header;
import coderslagoon.tclib.container.Volume;
import coderslagoon.tclib.crypto.AES256;
import coderslagoon.tclib.crypto.BlockCipher;
import coderslagoon.tclib.crypto.RIPEMD160;
import coderslagoon.tclib.util.Key;


public class VolumeTest {
    @Test
    public void testMinVol() throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream ins = getClass().getResourceAsStream("resources/minvol.tc");
        for (int b = ins.read(); b != -1; b = ins.read()) {
            bos.write(b);
        }
        ins.close();

        byte[] minvol = bos.toByteArray();

        final int SCT_SIZE = 512;
        final int IMG_SIZE = 281600;
        final int VOL_SIZE = IMG_SIZE - Header.SIZE * 2;

        assertTrue(IMG_SIZE == minvol.length);

        Header hdr = new Header(new Key.ByteArray("123".getBytes()), minvol, 0);

        assertEquals(hdr.blockCipher   , AES256.class);
        assertEquals(hdr.hashFunction  , RIPEMD160.class);
        assertEquals(hdr.sizeofVolume  , VOL_SIZE);
        assertEquals(hdr.dataAreaSize  , VOL_SIZE);
        assertEquals(hdr.dataAreaOffset, Header.SIZE);

        Volume vol0 = new Volume(BlockCipher.Mode.DECRYPT, hdr);
        Volume vol = (Volume)vol0.clone();
        vol0.erase();

        assertTrue(SCT_SIZE == vol.blockSize());
        assertTrue(0 == IMG_SIZE % vol.blockSize());

        long no = Header.BLOCK_COUNT;
        long end = no + (VOL_SIZE / vol.blockSize());

        System.out.printf("decrypting %d blocks...\n", end - no);

        for (; no < end; no++) {
            int ofs = (int)no * vol.blockSize();

            vol.processBlock(no, minvol, ofs);
        }

        final byte[] SOME_TEXT = "GALLIA est omnis divisa in partes tres".getBytes();

        assertTrue(BinUtils.arraysEquals(
                minvol,
                (int)(end - 2) * SCT_SIZE,
                SOME_TEXT,
                0,
                SOME_TEXT.length));

        final byte[] INIT_DATA = BinUtils.hexStrToBytes("eb3c904d53444f53");

        assertTrue(BinUtils.arraysEquals(
                minvol,
                Header.SIZE,
                INIT_DATA,
                0,
                INIT_DATA.length));

        long csz = Volume.sizeToContainerSize(1000);
        assertTrue(1000 < csz);
    }
}
