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

public class VolumeDescriptorPointer extends Descriptor {
    public int              volumeDescriptorSequenceNumber;
    public ExtentDescriptor nextVolumeDescriptorSequenceExtent;
    public BytePtr          reserved;    

    public final static int RESV_LEN = 484;
    
    protected VolumeDescriptorPointer(Tag tag, byte[] buf, int ofs) throws UDFException {
        super(tag);

        this.volumeDescriptorSequenceNumber     = BinUtils.readInt32LE(buf, ofs);   ofs += 4;
        this.nextVolumeDescriptorSequenceExtent = ExtentDescriptor.parse(buf, ofs); ofs += ExtentDescriptor.LENGTH; 
        this.reserved                           = new BytePtr.Checked(buf, ofs, RESV_LEN);    
    }

    public String toString() {
        return String.format("VDP:tag=[%s],vdsn=%s,nvdse=[%s]",   
                this.tag,
                this.volumeDescriptorSequenceNumber,
                this.nextVolumeDescriptorSequenceExtent);
    }
}
