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

package coderslagoon.trupax.lib.prg;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;



import coderslagoon.baselib.io.BlockDeviceImpl;
import coderslagoon.tclib.util.Key;
import coderslagoon.tclib.util.Password;
import coderslagoon.tclib.util.TCLibException;
import coderslagoon.trupax.lib.io.filesystem.udf.Browser;
import coderslagoon.trupax.tc.TCReader;


public class TCBrowser extends Browser {
    public  final TCReader tcr;
    private final RandomAccessFile raf;
    
    public TCBrowser(File volume, Listener listener, int blockSz, String passw) throws IOException, TCLibException {
        this(volume, listener, blockSz, new Password(passw.toCharArray(), null)); 
    }
    
    public TCBrowser(File volume, Listener listener, int blockSz, Key key) throws IOException, TCLibException {
        this.raf = new RandomAccessFile(volume, "r");
        
        BlockDeviceImpl.FileBlockDevice fbd = new 
        BlockDeviceImpl.FileBlockDevice(this.raf, blockSz, -1L, true, false); 

        this.tcr = new TCReader(fbd, key, false);
        
        super.init(this.tcr, listener); 
    }
    
    public void close() throws IOException {
        this.tcr.close(false);
        this.raf.close();
    }
}
