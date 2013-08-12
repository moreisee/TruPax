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

import coderslagoon.baselib.util.Prp;

public final class CmdLnProps extends ExeProps {
    public final static String PFX = "trupax.cmdln.";  

    public final static Prp.Str  OPTS_PASSWORD   = new Password (PFX); 
    public final static Prp.Lng  OPTS_FREESPACE  = new FreeSpace(PFX);
    public final static Prp.Bool OPTS_VERBOSE    = new Prp.Bool(PFX + "verbose"   , false); 
    public final static Prp.Bool OPTS_WIPE       = new Prp.Bool(PFX + "wipe"      , false); 
    public final static Prp.Bool OPTS_WIPEONLY   = new Prp.Bool(PFX + "wipeonly"  , false); 
    public final static Prp.Bool OPTS_EXTRACT    = new Prp.Bool(PFX + "extract"   , false); 
    public final static Prp.Bool OPTS_INVALIDATE = new Prp.Bool(PFX + "invalidate", false);
}
