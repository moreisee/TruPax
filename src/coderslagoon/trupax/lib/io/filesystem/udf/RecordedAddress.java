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

public class RecordedAddress { // aka lb_addr
    public final static int LENGTH = 6;
    
    public int   logicalBlockNumber;
    public short partitionReferenceNumber;
    
    public static RecordedAddress ZERO = new RecordedAddress(0, (short)0);
    
    RecordedAddress() {
    }

    public RecordedAddress(int logicalBlockNumber, short partitionReferenceNumber) {
        this.logicalBlockNumber       = logicalBlockNumber;
        this.partitionReferenceNumber = partitionReferenceNumber;
    }
    
    public static RecordedAddress parse(byte[] data, int ofs) {
        RecordedAddress result = new RecordedAddress();
        
        result.logicalBlockNumber       = BinUtils.readInt32LE(data, ofs);
        result.partitionReferenceNumber = BinUtils.readInt16LE(data, ofs + 4);
        
        return result;
    }
    
    public void write(byte[] buf, int ofs) {
        BinUtils.writeInt32LE(this.logicalBlockNumber      , buf, ofs);
        BinUtils.writeInt16LE(this.partitionReferenceNumber, buf, ofs + 4);
    }
    
    public String toString() {
        return String.format("RA:lbn=%s,prn=%s",    
                BinUtils.u32ToLng(this.logicalBlockNumber),
                BinUtils.u16ToInt(this.partitionReferenceNumber));
    }
}
