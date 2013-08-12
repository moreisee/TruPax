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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import coderslagoon.baselib.util.BinUtils;
import coderslagoon.tclib.util.Key;
import coderslagoon.tclib.util.Password;


public class PasswordTest {
    @Test
    public void test0() throws Exception {
        Password p = new Password("123".toCharArray(), null);

        byte[] enc = p.data();

        assertNotNull(enc);
        assertTrue(BinUtils.arraysEquals("123".getBytes(), enc));

        final String NOASCII = "h\u00e4user";

        p.erase();
        p = new Password(NOASCII.toCharArray(), null);

        enc = p.data();

        assertTrue(enc.length == NOASCII.length());
        assertTrue(BinUtils.arraysEquals(NOASCII.getBytes("ISO-8859-1"), enc));

        p.erase();
        p = new Password(NOASCII.toCharArray(), "UTF-8");

        enc = p.data();

        assertTrue(enc.length != NOASCII.length());
        assertTrue(BinUtils.arraysEquals(NOASCII.getBytes("UTF-8"), enc));

        p.erase();

        try {
            p.data();
            fail();
        }
        catch (Key.ErasedException kee) {
        }
    }
}
