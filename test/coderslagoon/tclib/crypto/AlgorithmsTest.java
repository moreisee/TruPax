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

import static org.junit.Assert.*;

import org.junit.Test;

import coderslagoon.tclib.crypto.AES256;
import coderslagoon.tclib.crypto.Algorithm;
import coderslagoon.tclib.crypto.CRC32;
import coderslagoon.tclib.crypto.HMAC;
import coderslagoon.tclib.crypto.PKCS5;
import coderslagoon.tclib.crypto.RIPEMD160;
import coderslagoon.tclib.crypto.XTS;
import coderslagoon.tclib.util.Testable;


public class AlgorithmsTest {
    @SuppressWarnings("unchecked")
    @Test
    public void test0() throws Throwable {
        for (Class<Testable> clz : new Class[] {
                CRC32       .class,
                AES256      .class,
                RIPEMD160   .class,
                HMAC        .class,
                PKCS5.PBKDF2.class,
                XTS         .class,
        }) {
            Testable tst = clz.newInstance();
            tst.test();
            if (tst instanceof Algorithm) {
                String name = ((Algorithm)tst).name();
                assertTrue(null == name ^ (null != name && 0 < name.length()));
            }
        }
    }

    @Test
    public void testPBKDF2Performance() {

        PKCS5.PBKDF2 pbkdf2 = new PKCS5.PBKDF2(new RIPEMD160());

        long delta, start = System.currentTimeMillis();

        long setups = 0;
        for (;10000 > (delta = System.currentTimeMillis() - start); setups++) {
            assertTrue(48 == pbkdf2.deriveKey(
                    "password".getBytes(), "salty\r\n".getBytes(),
                    2000, 48).length);
        }
        assertTrue(0 < setups);
        long rate = (setups * 1000000) / delta;
        System.out.printf("%d.%03d setups/second\n", rate / 1000, rate % 1000);
    }
}
