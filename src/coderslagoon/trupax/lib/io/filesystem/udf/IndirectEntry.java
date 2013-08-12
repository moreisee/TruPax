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

public class IndirectEntry extends Descriptor {
    public ICBTag                    icbTag;
    public AllocationDescriptor.Long indirectICB;
    
    public IndirectEntry(Tag tag, byte[] buf, int ofs) throws UDFException {
        super(tag);
        
        this.icbTag      = ICBTag.parse(buf, ofs); ofs += ICBTag.LENGTH;
        this.indirectICB = AllocationDescriptor.Long.parse(buf, ofs);
    }
    
    public String toString() {
        return String.format("IE:tag=[%s],it=[%s],iicb=[%s]",    
                this.tag,
                this.icbTag,
                this.indirectICB);
    }
}
