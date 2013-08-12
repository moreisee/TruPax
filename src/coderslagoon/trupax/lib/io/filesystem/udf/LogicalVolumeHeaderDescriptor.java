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
import coderslagoon.baselib.util.BytePtr;

public class LogicalVolumeHeaderDescriptor {
    public long    uniqueID;
    public BytePtr reserved;
    
    public final static int RESV_LEN = 24;
    public final static int LENGTH   = 32;
    
    public LogicalVolumeHeaderDescriptor() {
    }
    
    public LogicalVolumeHeaderDescriptor(byte[] buf, int ofs) {
        this.uniqueID = BinUtils.readInt64LE(buf, ofs);             ofs += 8;
        this.reserved = new BytePtr.Checked (buf, ofs, RESV_LEN);
    }

    public void write(byte[] buf, int ofs) {
        BinUtils     .writeInt64LE(this.uniqueID, buf, ofs); ofs += 8;
        this.reserved.write       (               buf, ofs); 
    }
    
    public BytePtr data() {
        byte[] result = new byte[LENGTH];
        write(result, 0);
        return new BytePtr(result);
    }
    
    public String toString() {
        return String.format("LVHD:uid=%d", this.uniqueID);     
    }
}
