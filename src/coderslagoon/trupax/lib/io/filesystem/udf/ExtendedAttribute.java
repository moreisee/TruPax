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

import java.util.ArrayList;

import coderslagoon.baselib.util.BinUtils;
import coderslagoon.baselib.util.BytePtr;

public class ExtendedAttribute {
    public int     type;
    public byte    subType;
    public BytePtr reserved;
    public int     length;
    public BytePtr data;
    
    public final static int RESV_LEN = 3;
    
    final static int INTRO_LEN = 4 + 1 + RESV_LEN + 4;
    
    public ExtendedAttribute(byte[] buf, int ofs) {
        this.type     = BinUtils.readInt32LE(buf, ofs);  ofs += 4;
        this.subType  = buf[ofs];                        ofs++;
        this.reserved = new BytePtr(buf, ofs, RESV_LEN); ofs += RESV_LEN;
        this.length   = BinUtils.readInt32LE(buf, ofs);  ofs += 4;
        this.data     = new BytePtr.Checked(buf, ofs, this.length - INTRO_LEN);
    }
    
    public int length() {
        return INTRO_LEN + this.data.len;
    }

    public static ExtendedAttribute[] read(BytePtr buf) throws UDFException {
        ArrayList<ExtendedAttribute> result = new
        ArrayList<ExtendedAttribute>();
        
        int ofs = buf.ofs;
        for (;;) {
            final int c = ofs - buf.ofs;
            if (c == buf.len) {
                break;
            }
            else if (c > buf.len) {
                throw new UDFException("extended attribute overflow (%d bytes)", c - buf.len);  
            }
            ExtendedAttribute ea = new ExtendedAttribute(buf.buf, ofs);
            ofs += ea.length();
            result.add(ea);
        }
        return result.toArray(new ExtendedAttribute[result.size()]);
    }

    public String toString() {
        return String.format("EA:typ=%d,styp=%d,rsv=0x%s,len=%d,data=0x%s",    
                this.type,
                (int)this.subType & 0x0ff,
                BinUtils.bytesToHexString(this.reserved),
                this.length,
                BinUtils.bytesToHexString(this.data));
    }
}
