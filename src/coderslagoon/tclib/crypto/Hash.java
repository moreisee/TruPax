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

/**
 * Hash algorithm abstraction.
 */
public interface Hash {
    /**
     * Minimum interface for a hash value producer.
     */
    public interface Producer extends Algorithm {
        /**
         * @return size of the hash, in bytes.
         */
        int hashSize();
        /**
         * Feed data into the instance.
         * @param buf The data buffer.
         * @param ofs Where to start reading in the buffer.
         * @param len Number of bytes to read out.
         */
        void update(byte[] buf, int ofs, int len);
        /**
         * Produces the final hash. The output must be of sufficient size.
         * @param hash Where to store the hash value.
         * @param ofs Where to start writing out the hash value.
         */
        void hash(byte[] hash, int ofs);
    }
    /**
     * Hash function definition as needed for TrueCrypt functionality.
     */
    public interface Function extends Producer {
        /**
         * Reset the instance, can be reused due to that.
         */
        void reset();
        /**
         * @return Size of the blocks the function consumes at a time.
         */
        int blockSize();
        /**
         * @return How often the function should be repeated for HMAC purposes.
         * This is the value which makes brute force password guessing slow.
         */
        int recommededHMACIterations();
    }
}
