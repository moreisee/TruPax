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

package coderslagoon.trupax.exe.util;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import coderslagoon.trupax.exe.util.PasswordCache;

public class PasswordCacheTest {
    
    @Test
    public void testPasswordCache() {
        PasswordCache pc = new PasswordCache();
        Assert.assertNull(pc.get());
        pc.clear();
        Assert.assertNull(pc.get());
        pc.set("");
        Assert.assertEquals(pc.get(), "");
        Assert.assertEquals(pc.get(), "");
        pc.clear();
        Assert.assertNull(pc.get());
        Random rnd = new Random(0xccddeeff00112233L);
        for (int len : new int[] { 0, 1, 3, 4, 5, 7, 8, 9, 14, 15, 16, 23, 24, 25, 1023, 49137 }) {
            char[] passwb = new char[len];
            for (int i = 0; i < passwb.length; i++) {
                passwb[i] = (char)((rnd.nextInt() & 4095) + ' ');
            }
            String passw = new String(passwb);
            Assert.assertEquals(len, passw.length());
            Assert.assertNull(pc.get());
            pc.set(passw);
            String passw2 = pc.get();
            Assert.assertEquals(passw2, passw);
            Assert.assertTrue(passw2 != passw);
            pc.clear();
        }
    }
}
