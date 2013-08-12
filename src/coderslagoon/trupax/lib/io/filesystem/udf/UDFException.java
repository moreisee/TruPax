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

import java.io.IOException;

public class UDFException extends IOException {
    public UDFException()                      { super(); }
    public UDFException(String s, Throwable t) { super(s, t); }
    public UDFException(String s)              { super(s); }
    public UDFException(Throwable t)           { super(t); }
    public UDFException(String fmt, Object... args) {
        super(String.format(fmt, args));
    }

    private static final long serialVersionUID = -9210861877102421673L;
}
