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

package coderslagoon.tclib.apps;

import static org.junit.Assert.*;

import java.io.File;
import java.security.SecureRandom;
import org.junit.Test;

import coderslagoon.tclib.container.Header;
import coderslagoon.trupax.sdk.apps.MakeEmptyVolume;


public class MakeEmptyVolumeTest {

    @Test
    public void testMain() throws Exception{
        File vol = new File(
            System.getProperty("java.io.tmpdir"), 
            String.format("empty_%d_%08x.tc", 
                System.currentTimeMillis(), new SecureRandom().nextInt()));
        final long SIZE = 10240000; 
        String[] args = new String[] {
            vol.getAbsolutePath(),
            String.valueOf(SIZE),
            "abc123"
        };
        MakeEmptyVolume.ExitCode ec = MakeEmptyVolume._main(args);
        assertEquals(MakeEmptyVolume.ExitCode.SUCCESS, ec);
        assertTrue(vol.exists());
        assertEquals(Header.BLOCK_COUNT * Header.BLOCK_SIZE + SIZE, vol.length());
        assertTrue(vol.delete());
    }
}
