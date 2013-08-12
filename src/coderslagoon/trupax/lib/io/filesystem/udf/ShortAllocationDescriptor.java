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

public class ShortAllocationDescriptor { // aka short_ad
    public enum ExtentType {
        RECORDED_AND_ALLOCATED,
        NOT_RECORDED_BUT_ALLOCATED,
        NOT_RECORDED_AND_NOT_ALLOCATED,
        NEXT_EXTENT_OF_ALLOCATION_DESCRIPTOPS;        
    };
    
    public final static int LENGTH = 8;
    
    public int        extentLength;
    public int        extentPosition;
    public ExtentType extentType;
    
    public static ShortAllocationDescriptor parse(byte[] buf, int ofs) throws UDFException {
        ShortAllocationDescriptor result = new ShortAllocationDescriptor();
        
        int extLen = BinUtils.readInt32LE(buf, ofs); ofs += 4;
        
        result.extentPosition = BinUtils.readInt32LE(buf, ofs);
        result.extentLength   = extLen & 0x3fffffff;
        result.extentType     = ExtentType.values()[extLen >>> 30];
        
        if (0 == result.extentLength && 
            ExtentType.RECORDED_AND_ALLOCATED != result.extentType) {
            throw new UDFException("illegal extent length 0x%08x in SAD", extLen); 
        }
        return result;
    }
    
    public String toString() {
        return String.format("SAD:el=%s,ep=%s", 
                BinUtils.u32ToLng(this.extentLength), 
                BinUtils.u32ToLng(this.extentPosition)); 
    }
}
