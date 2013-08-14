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

package coderslagoon.trupax.tc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import coderslagoon.baselib.io.BlockDevice;
import coderslagoon.baselib.io.BlockDeviceImpl;
import coderslagoon.baselib.io.MultiplexBlockDevice;
import coderslagoon.baselib.util.Prp;
import coderslagoon.tclib.container.Header;
import coderslagoon.tclib.container.Volume;
import coderslagoon.tclib.crypto.BlockCipher;
import coderslagoon.tclib.crypto.Rand;
import coderslagoon.tclib.crypto.Registry;
import coderslagoon.tclib.util.Key;
import coderslagoon.tclib.util.TCLibException;

public class TCBlockDevice extends BlockDeviceImpl {
    final BlockDevice  bdev;
    final BlockDevice  mpbdev;
    final byte[]       headerBackup;
    final List<Volume> volumes = new ArrayList<Volume>();

    ///////////////////////////////////////////////////////////////////////////
    
    final static String PROPS_PFX = "tcblockdevice.";
    
    final Prp.Int propBufSize;
    final Prp.Int propBufCount;

    ///////////////////////////////////////////////////////////////////////////
    
    final protected Boolean usedBackupHeader;

    ///////////////////////////////////////////////////////////////////////////

    public TCBlockDevice(BlockDevice bdev, 
                         Key key,
                         String hashFunction,
                         String blockCipher) throws IOException, TCLibException {
        this(bdev, key, hashFunction, blockCipher, null);
    }
    
    public TCBlockDevice(BlockDevice bdev, 
                         Key key,
                         String hashFunction,
                         String blockCipher,
                         Rand rnd) throws IOException, TCLibException {
        super(null == rnd, 
              null != rnd, 
              false, 
              bdev.size() - 2 * Header.BLOCK_COUNT, 
              bdev.blockSize());
        
        this.propBufSize  = new Prp.Int(PROPS_PFX + "bufsize", 64);  
        this.propBufCount = new Prp.Int(PROPS_PFX + "bufcount",
                Runtime.getRuntime().availableProcessors() + 1);        

        this.bdev = bdev;
        
        final Volume vol0;
        
        if (null == rnd) {
            Header hdr = null;
            int bak = 0;
            for (bak = 0; bak < 2; bak++) {
                byte[] hdrData = new byte[Header.SIZE];
                
                long i = 0 == bak ? 0 : (bdev.size() - Header.BLOCK_COUNT);
                long c = i + Header.BLOCK_COUNT;
                
                for (int ofs = 0; i < c; i++, ofs += bdev.blockSize()) {
                    bdev.read(i, hdrData, ofs);
                }
                
                try {
                    hdr = new Header(key, hdrData, 0);
                    break;
                }
                catch (TCLibException tle) {
                    if (0 != bak) {
                        throw tle;
                    }
                }
            }
            this.usedBackupHeader = (0 != bak);
            this.headerBackup = null;
            
            vol0 = new Volume(BlockCipher.Mode.DECRYPT, hdr);
        }
        else {
            Header hdr = new Header(Registry._hashFunctions.lookup(hashFunction),
                                    Registry._blockCiphers .lookup(blockCipher));
            
            hdr.generateSalt(rnd);
            hdr.generateKeyMaterial(rnd);
            
            hdr.version        = Header.Version.LOWEST_HEADER;
            hdr.minimumVersion = Header.Version.LOWEST_APP;
     
            rnd.make(hdr.salt);
            
            final long volumeBytes = this.size * bdev.blockSize();
            if (0L > volumeBytes) {
                throw new IOException();
            }
    
            hdr.sizeofHiddenVolume = 0L;
            hdr.sizeofVolume       = volumeBytes;
            hdr.dataAreaOffset     = Header.OFS_DATA_AREA;
            hdr.dataAreaSize       = volumeBytes;
            hdr.flags              = 0;
            hdr.reserved3          = null;
            hdr.hiddenVolumeHeader = null;
            
            final byte[] passw = key.data();
            
            writeHeaderData(0, hdr.encode(passw));
    
            vol0 = new Volume(BlockCipher.Mode.ENCRYPT, hdr);
            
            hdr.generateSalt(rnd);
            this.headerBackup = hdr.encode(passw);
            
            hdr.erase();
            
            this.usedBackupHeader = null;
        }
        
        this.volumes.add(vol0);
            
        this.mpbdev = new MultiplexBlockDevice(
                new BlockDevice.Filter.Factory() {
                    public Filter createRead() {
                        return createWrite();
                    }
                    public Filter createWrite() {
                        final Volume vol = (Volume)vol0.clone();
                        TCBlockDevice.this.volumes.add(vol);
                        
                        return new Filter() {
                            public void transform(long num, byte[] block, 
                                    int ofs) throws IOException {
                                try {
                                    vol.processBlock(num, block, ofs);
                                }
                                catch (TCLibException tcle) {
                                    throw new IOException(tcle);
                                }
                            }
                            public long map(long num) {
                                return num + Header.BLOCK_COUNT;
                            }
                        };
                    }
                    public void initialize(int blockSize) throws IOException {
                        // NOTE: we could have done the setup here as well
                    }
                },
                new BlockDevice() {
                    // (mapping to the new block number already done by the filter)
                    public int blockSize() {
                        return TCBlockDevice.this.blockSize();
                    }
                    public void close(boolean err) throws IOException {
                    }
                    public void read(long num, byte[] block, int ofs) throws IOException {
                        TCBlockDevice.this.bdev.read(num, block, ofs);
                    }
                    public boolean readOnly   () { return TCBlockDevice.this.readOnly   (); }
                    public boolean writeOnly  () { return TCBlockDevice.this.writeOnly  (); }
                    public boolean serialWrite() { return TCBlockDevice.this.serialWrite(); }
                    public long    size       () { return TCBlockDevice.this.size       (); }

                    public void write(long num, byte[] block, int ofs) throws IOException {
                        TCBlockDevice.this.bdev.write(num, block, ofs);
                    }
                },
                this.propBufCount.get(),
                this.propBufSize .get());
    }

    ///////////////////////////////////////////////////////////////////////////

    /** @see coderslagoon.baselib.io.BlockDevice#close() */
    public void close(boolean err) throws IOException {
        this.mpbdev.close(err);
        
        if (!err && !decrypting()) {
            writeHeaderData(this.bdev.size() - Header.BLOCK_COUNT, 
                            this.headerBackup);
        }
            
        this.bdev.close(err);
        
        for (Volume vol : this.volumes) {
            vol.erase();
        }
    }

    /** @see coderslagoon.baselib.io.BlockDeviceImpl#internalRead(long, byte[], int) */
    protected void internalRead(long num, byte[] block, int ofs) throws IOException {
        this.mpbdev.read(num, block, ofs);
    }

    /** @see coderslagoon.baselib.io.BlockDeviceImpl#internalWrite(long, byte[], int) */
    protected void internalWrite(long num, byte[] block, int ofs) throws IOException {
        this.mpbdev.write(num, block, ofs);
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    private void writeHeaderData(long num, byte[] data) throws IOException {
        for (int ofs = 0; ofs < data.length; ofs += this.bdev.blockSize()) {
            this.bdev.write(num++, data, ofs);
        }
    }
    
    private boolean decrypting() {
        return null == this.headerBackup;
    }
}
