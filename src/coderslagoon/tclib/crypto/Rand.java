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

import java.util.Arrays;
import java.util.Random;

import coderslagoon.baselib.util.BytePtr;
import coderslagoon.tclib.util.Erasable;
import coderslagoon.tclib.util.Testable;


/**
 * Definition of random number generators.
 */
public abstract class Rand implements Testable, Erasable {
    
    /**
     * Create new random data.
     * @param buf Buffer to store the random bytes.
     * @param ofs Where to start writing in the buffer.
     * @param len Number of bytes to write.
     */
    public abstract void make(byte[] buf, int ofs, int len);

    /**
     * Create new random data.
     * @param bp Pointer to the output area.
     */
    public void make(BytePtr bp) {
        make(bp.buf, bp.ofs, bp.len);
    }

    ///////////////////////////////////////////////////////////////////////////

    private static class Wrapper extends Rand {
        final static int RND_WRD_SZ = 4;    // must be 2^N (N>0, N<31)

        Random random;
        byte[] rndwrd = new byte[RND_WRD_SZ];

        protected Wrapper(Random random) {
            this.random = random;
        }

        @Override
        public void make(byte[] buf, int ofs, int len) {
            final Random random = this.random;
            final byte[] rndwrd = this.rndwrd;

            final int end = ofs + len;

            for (int c = end & ~(RND_WRD_SZ - 1); ofs < c; ofs += RND_WRD_SZ) {
                random.nextBytes(rndwrd);
                System.arraycopy(rndwrd, 0, buf, ofs, RND_WRD_SZ);
            }
            System.arraycopy(rndwrd, 0, buf, ofs, ofs - end);

            // we don't want to cache the leftover random data, since it might
            // become key material and thus should not exist for too long...
        }

        @Override
        public void test() {
            // TODO: could run a chi-square test or something similar
        }

        @Override
        public void erase() {
            Arrays.fill(this.rndwrd, (byte)0);

            // maybe this helps or it doesn't - we can't really tell
            this.random.setSeed(0L);
        }
    }

    /**
     * Wrap a standard framework RNG.
     * @param random The random number generator to wrap.
     * @return Wrapped instance.
     */
    public static Rand wrap(Random random) {
        return new Wrapper(random);
    }
}
