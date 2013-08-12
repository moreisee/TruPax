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

import coderslagoon.tclib.crypto.RIPEMD160;

public class RIPEMD160Perf {

    final static int  DATA_SZ    = 4096;
    final static int  LOOPS      = 1000; //0;
    final static long RUN_MILLIS = 8000;

    static void measurePerformance(RIPEMD160 re) {
        final long start = System.currentTimeMillis();

        byte[] testData = new byte[DATA_SZ];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte)i;
        }

        long tm;
        long c = 0;

        while ((tm = System.currentTimeMillis() - start) < RUN_MILLIS) {
            for (int i = 0; i < LOOPS; i++) {
                re.update(testData, 0, testData.length);
            }
            c += LOOPS;
        }

        System.out.printf("%,.1f bytes per second",
                ((double)(testData.length * c) * 1000.0) /
                 (double)Math.max(1, tm));
    }

    public static void main(String[] args) {
        final RIPEMD160 re = new RIPEMD160();
        try {
            re.test();
        }
        catch (Throwable err) {
            System.err.println("TEST FAILED: " + err.getMessage());
            return;
        }
        measurePerformance(re);
    }
}
