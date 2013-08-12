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

import java.util.Properties;

import coderslagoon.baselib.util.MiscUtils;
import coderslagoon.baselib.util.Prp;

public class ExeProps {
    public final static String EXE_PFX = "trupax.exe.";                
	
	public static class FreeSpace extends Prp.Lng {
        public FreeSpace(String pfx) {
			super(pfx + "freespace", 0L); 
		}
		public boolean validate(String raw) {
            return 0 <= MiscUtils.strToUSz(raw);
        }
		public Long get(Properties p) {
			return MiscUtils.strToUSz(p.getProperty(this.name, this.dflt.toString()));
		}
    };

    public static class Password extends Prp.Str {
        public Password(String pfx) {
            super(pfx + "password", null); 
        }
        public boolean validate(String raw) {
            return 0 < raw.length();
        }
    };

    public final static Prp.Str Lang = new Prp.Str(EXE_PFX  + "lang", null); 
}
