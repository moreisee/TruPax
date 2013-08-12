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

import coderslagoon.baselib.util.BinUtils;

public class ExtentDescriptor {   // aka extent_ad
    public final static int LENGTH = 8;
    
    public final static ExtentDescriptor NONE = new ExtentDescriptor(0, 0);
    
    public final static int MAX_LENGTH = 0x3fffffff;
    
    public final int length;
    public final int location;
    
    public ExtentDescriptor(int length, int location) {
        this.length   = length;
        this.location = location;
    }

    public static void checkLength(int length) throws UDFException {
        if (0 != (length & 0xc0000000)) {
            throw new UDFException(
                   "invalid extend descriptor length 0x%08x", length); 
        }
    }
    
    public static ExtentDescriptor parse(byte[] buf, int ofs) throws UDFException {
        ExtentDescriptor result = new ExtentDescriptor(
            BinUtils.readInt32LE(buf, ofs),
            BinUtils.readInt32LE(buf, ofs + 4));

        checkLength(result.length);

        return result;
    }
    
    public boolean none() {
        return 0 == this.location;
    }
    
    public void write(byte[] buf, int ofs) throws UDFException {
        checkLength(this.length);
        BinUtils.writeInt32LE(this.length  , buf, ofs);
        BinUtils.writeInt32LE(this.location, buf, ofs + 4);
    }
    
    public String toString() {
        return String.format("ED:len=%d,loc=%d",    
                BinUtils.u32ToLng(this.length), 
                BinUtils.u32ToLng(this.location));   
    }
}
