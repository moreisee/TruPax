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

package coderslagoon.trupax.sdk.apps;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.Arrays;

import coderslagoon.tclib.container.Header;
import coderslagoon.tclib.container.Volume;
import coderslagoon.tclib.crypto.AES256;
import coderslagoon.tclib.crypto.BlockCipher;
import coderslagoon.tclib.crypto.RIPEMD160;
import coderslagoon.tclib.crypto.Rand;
import coderslagoon.tclib.util.Password;

/**
 * Creates empty volumes (all zeros) with a single header. The volumes can then
 * be mounted with TrueCrypt and formatted by the operation system with a file
 * system of the user's choice.
 */
public class MakeEmptyVolume {
    
    public enum ExitCode {
        SUCCESS,
        EXISTS,
        ERROR,
        NO_PASSWORD,
        BAD_SIZE,
        NO_ARGS,
        NO_CONSOLE
    }

    public static ExitCode _main(String[] args) {
        if (3 != args.length) {
            System.out.println("usage  : tcmev [volume] [size] [password]\n" +
                               "example: tcmev test.tc 5120000 E4N2Jd6CUG7k");
            return ExitCode.NO_ARGS;
        }
        
        // get and align the volume size (to block border)
        long size = -1L;
        try {
            size = Long.parseLong(args[1]);
            if (size < Header.BLOCK_SIZE) {
                System.err.println("size too small");
                return ExitCode.BAD_SIZE;
            }
        }
        catch (NumberFormatException nfe) {
            System.err.println("invalid size");
            return ExitCode.BAD_SIZE;
        }
        size -= size % Header.BLOCK_SIZE;
        
        // check if the file exists
        File fl = new File(args[0]);
        if (fl.exists()) {
            System.err.println("file exists");
            return ExitCode.EXISTS;
        }
        
        char[] passw = args[2].toCharArray(); 
        ExitCode ec;
        try {
        
            // open the file
            RandomAccessFile raf = new RandomAccessFile(fl, "rw");
            
            // create and write the header
            Rand rnd = Rand.wrap(new SecureRandom());
            Header hdr = new Header(RIPEMD160.class, AES256.class);
            hdr.generateSalt(rnd);
            hdr.generateKeyMaterial(rnd);
            rnd.make(hdr.salt);
            hdr.sizeofHiddenVolume = 0L;
            hdr.sizeofVolume       = size;
            hdr.dataAreaOffset     = Header.OFS_DATA_AREA;
            hdr.dataAreaSize       = size;
            hdr.flags              = 0;
            hdr.reserved3          = null;
            hdr.hiddenVolumeHeader = null;
            hdr.version            = Header.Version.LOWEST_HEADER;
            hdr.minimumVersion     = Header.Version.LOWEST_APP;
            Password pwkey = new Password(passw, null);
            byte[] buf = hdr.encode(pwkey.data());
            raf.write(buf);
            
            // create the volume (the actual blocks encryption stage)
            Volume vol = new Volume(BlockCipher.Mode.ENCRYPT, hdr);
            
            // write the blocks (all zeros)
            size /= Header.BLOCK_SIZE;
            buf = new byte[Header.BLOCK_SIZE];
            for (long blk = 0; blk < size; blk++) {
                Arrays.fill(buf, (byte)0);
                vol.processBlock(blk, buf, 0);
                raf.write(buf);
            }
            
            // cleanup and close
            vol.erase();
            hdr.erase();
            raf.close();
            fl = null;
            ec = ExitCode.SUCCESS;
            System.out.println("done.");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            ec = ExitCode.ERROR; 
        }
        finally {
            if (null != fl) {
                fl.delete();
            }
        }
        return ec;
    }
    
    public static void main(String[] args) {
        System.exit( _main(args).ordinal());
    }
}
