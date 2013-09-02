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
import java.util.Iterator;
import org.junit.Test;

import coderslagoon.trupax.sdk.apps.PasswordSearch;
import coderslagoon.trupax.sdk.apps.PasswordSearch.GeneratorSource;

public class PasswordSearchTest {

    @Test
    public void testGeneratorSource() {
        Iterator<String> i = new GeneratorSource(3, GeneratorSource.Set.NUMBERS);
        int c = 0;
        while (i.hasNext()) {
            assertEquals(String.format("%03d",  c++), i.next());
        }
        assertEquals(1000, c);
    }
    
    final static String TEST_FILE_PATH = 
            "./test/coderslagoon/tclib/container/resources/";
    
    @Test
    public void testMainGenerator() {
        for (String etc : new String[] { "_", "" }) {
            String[] args = new String[] {
                    TEST_FILE_PATH + "firstsector.dat",
                "generator",
                "3",
                "user_defined",
                "abcd1234" + etc
            };
            PasswordSearch.ExitCode ec = PasswordSearch._main(args);
            if (0 == etc.length()) assertEquals(PasswordSearch.ExitCode.NOT_FOUND, ec);        
            else                   assertEquals(PasswordSearch.ExitCode.SUCCESS  , ec);
        }
    }
    
    @Test
    public void testMainFile() {
        String[] args = new String[] {
            TEST_FILE_PATH + "firstsector.dat",
            "file",
            TEST_FILE_PATH + "passwords.txt"
        };
        PasswordSearch.ExitCode ec = PasswordSearch._main(args);
        assertEquals(PasswordSearch.ExitCode.SUCCESS, ec);
    }
}
