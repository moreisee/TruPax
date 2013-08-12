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

public abstract class Console {
    public abstract char[]  readPassword(String fmt, Object... args); 
    public abstract Console format      (String fmt, Object... args);
    
    public final static Console system() {
        final java.io.Console con = System.console();
        
        return null == con ? null : new Console() {
            public char[] readPassword(String fmt, Object... args) {
                return con.readPassword(fmt, args);
            }
			public Console format(String fmt, Object... args) {
				con.format(fmt, args);
				return this;
			}
        };
    }
}
