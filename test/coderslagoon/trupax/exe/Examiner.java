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

package coderslagoon.trupax.exe;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import coderslagoon.test.util.TestUtils;
import coderslagoon.trupax.exe.CmdLn;
import coderslagoon.trupax.lib.UDFTest;
import coderslagoon.trupax.lib.prg.PrgImpl;
import coderslagoon.trupax.test.util.Verifier;


public class Examiner extends CmdLn {
    static void run(String[] args) throws Exception {
        __TEST_password = args[0];

        args = Arrays.copyOfRange(args, 1, args.length);

        String volume = null;
        for (String arg : args) {
            if (!arg.trim().startsWith("-")) {
                volume = arg;
                break;
            }
        }
        
        CmdLn.main(args);
        
        File tmpDir = TestUtils.createTempDir("examiner"); 
        File dump = new File(tmpDir, "dump");
                             
        Verifier.decryptVolume(__TEST_password.toCharArray(),
                               new File(volume), dump);

        final PrintStream ps = new PrintStream(new File(tmpDir, "udftest.txt"));
        if (UDFTest.available()) {
            if (UDFTest.exec(dump,
                             PrgImpl.BLOCK_SIZE, 
                             true, 
                             false, 
                             false, 
                             new UDFTest.Listener() {
                public boolean onOutput(String ln) {
                    ps.println(ln);
                    return true;
                }
            })) {
                System.out.println("OK");
            }
            else {
                System.err.println("UDFTEST FAILED!");
            }
        }
        ps.close();
    }
    
    public static void main(String[] args) {
        try {
            run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
