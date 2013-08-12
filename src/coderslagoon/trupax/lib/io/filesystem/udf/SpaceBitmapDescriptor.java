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

public class SpaceBitmapDescriptor extends Descriptor {
    public int numberOfBits;
    public int numberOfBytes;
    
    public SpaceBitmapDescriptor(int location) {
        super(new Tag(Tag.Identifier.SPACE_BITMAP_DESCRIPTOR, location));
    }        
    
    public SpaceBitmapDescriptor(Tag tag, byte[] buf, int ofs) throws UDFException {
        super(tag);
        
        this.numberOfBits  = BinUtils.readInt32LE(buf, ofs); ofs += 4;
        this.numberOfBytes = BinUtils.readInt32LE(buf, ofs); ofs += 4;
        
        if ((BinUtils.u32ToLng(this.numberOfBits) + 7L) / 8L >
             BinUtils.u32ToLng(this.numberOfBytes)) {
            throw new UDFException("number of bytes too low"); 
        }
    }
    
    public final static int LENGTH = Tag.LENGTH + 4 + 4;
    
    public int write(byte[] block, int ofs) {
        int ofs0 = ofs;
        ofs += Tag.LENGTH;

        BinUtils.writeInt32LE(this.numberOfBits , block, ofs); ofs += 4;
        BinUtils.writeInt32LE(this.numberOfBytes, block, ofs); ofs += 4;
        
        this.tag.write(block, ofs0, ofs - ofs0);         
        
        return ofs;
    }
    
    public String toString() {
        return String.format("SBD:tag=[%s],nbits=%s,nbytes=%s", 
                this.tag,
                BinUtils.u32ToLng(this.numberOfBits),
                BinUtils.u32ToLng(this.numberOfBytes));
    }    
}
