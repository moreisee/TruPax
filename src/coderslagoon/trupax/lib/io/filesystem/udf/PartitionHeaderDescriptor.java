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

import coderslagoon.baselib.util.BytePtr;

public class PartitionHeaderDescriptor {
    public AllocationDescriptor.Short unallocatedSpaceTable;
    public AllocationDescriptor.Short unallocatedSpaceBitmap;
    public AllocationDescriptor.Short partitionIntegrityTable;
    public AllocationDescriptor.Short freedSpaceTable;
    public AllocationDescriptor.Short freedSpaceBitmap;
    public BytePtr                    reserved;                    
    
    public final static int LENGTH   = 128;
    public final static int RESV_LEN = 88;

    public PartitionHeaderDescriptor() {
    }
    
    public PartitionHeaderDescriptor(byte[] buf, int ofs) throws UDFException {
        this.unallocatedSpaceTable   = AllocationDescriptor.Short.parse(buf, ofs); ofs += AllocationDescriptor.Short.LENGTH;
        this.unallocatedSpaceBitmap  = AllocationDescriptor.Short.parse(buf, ofs); ofs += AllocationDescriptor.Short.LENGTH;
        this.partitionIntegrityTable = AllocationDescriptor.Short.parse(buf, ofs); ofs += AllocationDescriptor.Short.LENGTH;
        this.freedSpaceTable         = AllocationDescriptor.Short.parse(buf, ofs); ofs += AllocationDescriptor.Short.LENGTH;
        this.freedSpaceBitmap        = AllocationDescriptor.Short.parse(buf, ofs); ofs += AllocationDescriptor.Short.LENGTH;
        this.reserved                = new BytePtr(buf, ofs, RESV_LEN);
    }
    
    public String toString() {
        return String.format("PHD:ust=[%s],usb=[%s],pit=[%s],fst=[%s],fsb=[%s]", 
                this.unallocatedSpaceTable,   
                this.unallocatedSpaceBitmap,  
                this.partitionIntegrityTable, 
                this.freedSpaceTable,         
                this.freedSpaceBitmap);        
    }

    public void write(byte[] buf, int ofs) throws UDFException {
        ofs = this.unallocatedSpaceTable  .write(buf, ofs);
        ofs = this.unallocatedSpaceBitmap .write(buf, ofs);  
        ofs = this.partitionIntegrityTable.write(buf, ofs); 
        ofs = this.freedSpaceTable        .write(buf, ofs);         
        ofs = this.freedSpaceBitmap       .write(buf, ofs);
        if (null != this.reserved) {
            this.reserved.write(buf, ofs);
        }
    }
}
