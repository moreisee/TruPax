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


public class VolumeStructureDescriptor {
    public final static String BEA01 = "BEA01"; 
    public final static String TEA01 = "TEA01"; 
    public final static String NSR02 = "NSR02"; 
    public final static String NSR03 = "NSR03"; 
    
    public final static int STD_ID_LEN = 5;
    
    public int     structureType;
    public String  standardIdentifier;
    public int     structureVersion;
    public BytePtr data;

    public VolumeStructureDescriptor() {
        this.structureVersion = 1;
    }
    
    public VolumeStructureDescriptor(byte[] block, int ofs, int len) {
        if (len < 1 + STD_ID_LEN + 1) {
            throw new IllegalArgumentException();
        }
        
        int end = ofs + len;
        
        this.structureType = block[ofs++];
        
        // NOTE: will that always be ASCII, compatible to UTF-8?
        this.standardIdentifier = new String(block, ofs, STD_ID_LEN);
        ofs += STD_ID_LEN;

        this.structureVersion = block[ofs++];
        
        this.data = new BytePtr.Checked(block, ofs, end - ofs);
    }
    
    public boolean check() {
        if (!this.standardIdentifier.equals(BEA01) &&
            !this.standardIdentifier.equals(TEA01) &&
            !this.standardIdentifier.equals(NSR02) &&
            !this.standardIdentifier.equals(NSR03)) {
            return false;   
        }
        if (0 != this.structureType ||
            1 != this.structureVersion) {
            return false;   
        }
        return true;
    }
    
    public int write(byte[] block, int ofs) {
        block[ofs++] = (byte)this.structureType;
        
        System.arraycopy(this.standardIdentifier.getBytes(), 0, block, ofs, STD_ID_LEN);
        ofs += STD_ID_LEN;
        
        block[ofs++] = (byte)this.structureVersion;
        
        if (null != this.data) {
            System.arraycopy(this.data.buf, 
                             this.data.ofs, 
                             block, 
                             ofs, 
                             this.data.len);
            ofs +=           this.data.len;
        }
        
        return ofs;
    }
    
    public String toString() {
        return String.format("VSD:type=%d,id=%s,ver=%d",  
                BinUtils.u32ToLng(this.structureType),
                this.standardIdentifier,
                BinUtils.u32ToLng(this.structureVersion));
    }
}
