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

import coderslagoon.tclib.crypto.BlockCipher;
import coderslagoon.tclib.crypto.XTS;
import coderslagoon.tclib.util.Erasable;
import coderslagoon.tclib.util.TCLibException;


/**
 * TrueCrypt volume encryption or decryption.
 */
public class Volume implements Erasable, Cloneable {
    XTS xts;

    /**
     * Creates a new volume instance.
     * @param mode Mode to either encrypt or decrypt.
     * @param header The header to use. Writing of the header(s) to the
     * beginning and the end of the volume must be handled by the caller itself.
     * @throws TCLibException If any error occurred.
     */
    public Volume(BlockCipher.Mode mode, Header header) throws TCLibException {
        BlockCipher bc1 = null;
        BlockCipher bc2 = null;
        try {
            bc1 = header.blockCipher.newInstance();
            bc2 = header.blockCipher.newInstance();

            bc1.initialize(mode                    , header.keyMaterial.buf, header.keyMaterial.ofs);
            bc2.initialize(BlockCipher.Mode.ENCRYPT, header.keyMaterial.buf, header.keyMaterial.ofs + bc1.keySize());

            this.xts = new XTS(bc1, bc2);
        }
        catch (InstantiationException ie) {
            throw new TCLibException(ie);
        }
        catch (IllegalAccessException iae) {
            throw new TCLibException(iae);
        }
        finally {
            if (null == this.xts) {
                if (null != bc1) bc1.erase();
                if (null != bc2) bc2.erase();
            }
        }
    }

    /**
     * @return Size of block of a volume.
     */
    public int blockSize() {
        return XTS.DATA_UNIT_SIZE;
    }

    /**
     * Encrypts or decrypts a block.
     * @param number The block number (0..N-1, N=size of volume in blocks).
     * @param blk Buffer holding the block's data.
     * @param ofs Where the block data starts
     * @throws TCLibException If any error occurred.
     */
    public void processBlock(long number, byte[] blk, int ofs) throws TCLibException {
        this.xts.process(blk, ofs, blockSize(), number, 0);
    }

    /** @see coderslagoon.tclib.util.Erasable#erase() */
    public void erase() {
        this.xts.erase();
    }

    //////////////////////////////////////////////////////////////////////////

    /**
     * To compute the actual size a container must have, meaning the typical.
     * [header][volume][header] format.
     * @param size Size of the volume in blocks.
     * @return Size of the container in blocks.
     */
    public static long sizeToContainerSize(long size) {
        return Header.BLOCK_COUNT + size + Header.BLOCK_COUNT;
    }

    //////////////////////////////////////////////////////////////////////////

    private Volume() {
    }

    /** @see java.lang.Object#clone() */
    public Object clone() {
        Volume result = new Volume();
        result.xts = (XTS)this.xts.clone();
        return result;
    }
}
