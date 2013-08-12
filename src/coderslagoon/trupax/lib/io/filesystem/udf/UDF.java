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

package coderslagoon.trupax.lib.io.filesystem.udf;

public interface UDF {
    public final static int    VOLUME_SPACE_INIT_SIZE = 32768;
    public final static long   ROOT_FILENTRY_UID = 0L;
    public final static int    MIN_UNIQUE_ID = 16;
    public final static int    MAX_FILENAME_DSTRLEN = 255;
    public final static int    MAX_PATH_LEN = 1023;
    public final static String ENCODING_UTF8 = "UTF-8";
    
    public enum Compliance {
        STRICT(10),
        VISTA(9);
        Compliance(int level) {
            this.level = level;
        }
        int level;
        public static Compliance _default = Compliance.STRICT;
        public static boolean is(Compliance cmpl) {
            return _default.level <= cmpl.level;
        }
        public static void setTo(Compliance cmpl) {
            _default = cmpl;
        }
    }
}
