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

package coderslagoon.trupax.lib.prg;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import coderslagoon.tclib.crypto.Registry;
import coderslagoon.trupax.lib.prg.PrgProps;
import coderslagoon.trupax.lib.prg.Prg.PropertyInfo;


public class PrgPropsTest {
    @Before
    public void setup() throws Exception {
        Registry.setup(false);
    }

    @After
    public void tearDown() {
        Registry.clear();
    }
    
    @Test
    public void test0() throws Exception {
        PrgProps p = new PrgProps();
        
        PropertyInfo pinf = p.getInfo("trupax.prg.recursivesearch");
        assertNotNull(pinf);
        assertTrue(pinf.type == PropertyInfo.Type.FLAG);
        
        assertNull(p.getInfo("doesnotexist"));
        
        pinf = p.getInfo("trupax.prg.blockcipher");
        assertNotNull(pinf);
        assertTrue(pinf.type == PropertyInfo.Type.SELECT);
        assertTrue(0 < pinf.selection.length);
        assertTrue(pinf.selection[0].equals("AES256"));
    }
}
