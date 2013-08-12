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

package coderslagoon.tclib.util;

import java.util.Arrays;

/**
 * Key holder. To ensure that sensitive key material gets erased safely.
 */
public interface Key extends Erasable {
    
    /**
     * @return The key material. Not a copy, so be careful.
     * @throws ErasedException If the key has been erased already.
     */
    public byte[] data() throws ErasedException;

    /** To detect already-erased issue. */
    public static class ErasedException extends TCLibException {
        private static final long serialVersionUID = 2600402929643683456L;
    }

    /**
     * Key which is wrapping a byte array 
     */
    public static class ByteArray implements Key {
        protected byte[] data;

        protected ByteArray() {
        }

        public ByteArray(byte[] key) {
            this.data = key;
        }

        @Override
        public byte[] data() throws ErasedException {
            if (null == this.data) {
                throw new ErasedException();
            }
            else {
                return this.data;
            }
        }

        @Override
        public void erase() {
            if (null != this.data) {
                Arrays.fill(this.data, (byte)0);
                this.data = null;
            }
        }
    }
}
