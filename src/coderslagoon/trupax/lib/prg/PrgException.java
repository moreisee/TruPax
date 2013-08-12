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

import coderslagoon.baselib.util.BaseLibException;

public class PrgException extends BaseLibException {
    public PrgException(String fmt, Object... args) {
        super(fmt, args);
    }
    public PrgException(Throwable cause, String fmt, Object... args) {
        super(cause, fmt, args);
    }
    private static final long serialVersionUID = -4050668908929358077L;
}
