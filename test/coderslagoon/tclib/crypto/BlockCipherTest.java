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

package coderslagoon.tclib.crypto;

import static org.junit.Assert.assertTrue;
import coderslagoon.tclib.crypto.BlockCipher;

public class BlockCipherTest {


    public void test0() {


        BlockCipher bc = new BlockCipher() {
            @Override
            public String name() {
                return null;
            }
            @Override
            public void erase() {
            }
            @Override
            public void test() throws Throwable {
            }
            @Override
            public int blockSize() {
                return 0;
            }
            @Override
            public int keySize() {
                return 0;
            }
            @Override
            public void processBlock(byte[] in, int ofs_i, byte[] out, int ofs_o) {
            }
            @Override
            public Object clone() {
                return null;
            }
        };
        bc.initialize(BlockCipher.Mode.ENCRYPT, null,  0);
        assertTrue(BlockCipher.Mode.ENCRYPT == bc.mode());
    }
}
