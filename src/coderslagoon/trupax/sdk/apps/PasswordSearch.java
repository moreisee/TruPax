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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import coderslagoon.tclib.container.Header;
import coderslagoon.tclib.container.Header.NoMatchingAlgorithmException;
import coderslagoon.tclib.util.Password;
import coderslagoon.tclib.util.TCLibException;

/**
 * Simple command line password search solution for TC containers. There is a
 * lot of room for improvement regarding the generators. Running publicly known
 * password lists probably promises better chances for success. Notice that the
 * searcher uses all cores in the system to achieve the maximum possible speed.
 * But notice also that due to the computation-intensive password setup the
 * actual speed is slow. At this point a few hundred passwords per second is
 * the best case scenario.
 */
public class PasswordSearch {

    /** Exit codes. The ordinal values get surfaced to the caller. */
    public enum ExitCode {
        /** Password was discovered. */
        SUCCESS,
        /** No password was found. */
        NOT_FOUND,
        /** Something problem with invalid arguments. */
        WRONG_ARGS,
        /** An I/O error occurred, either with the container file or with the
         * password list file. */
        IO_ERROR,
        /** A general error/crash did happen. */
        ERROR
    }
    
    ///////////////////////////////////////////////////////////////////////////

    /** 
     * Basic password source interface. A source is nothing else than an
     * iterator popping out passwords.
     */
    public interface Source extends Iterator<String> {
        /** Closes the source, releases potential resources. */
        void close();
    }
    
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Source which reads passwords from line-bases streams. One password per
     * line. For encoding UTF-8 is assumed.
     */
    public static class StreamSource implements Source {
        /**
         * Default constructor.
         * @param ins The input stream to read from. Taken over.
         * @throws IOException If the stream causes any I/O error.
         */
        public StreamSource(InputStream ins) throws IOException {
            this.lnr = new LineNumberReader(new InputStreamReader(ins));
            this.next = this.lnr.readLine();
        }
        @Override
        public boolean hasNext() {
            return null != this.next;
        }
        @Override
        public String next() {
            String result = this.next;
            if (null != result) {
                try {
                    this.next = this.lnr.readLine();
                }
                catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                    System.exit(ExitCode.IO_ERROR.ordinal());
                }
            }
            return result;
        }
        @Override
        public void remove() {
        }
        @Override
        public void close() {
            try {
                this.lnr.close();
            }
            catch (IOException ignored) {
            }
        }
        final LineNumberReader lnr;
        String next;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Generator which  produces passwords based on a certain length and a set
     * of characters. All of the permutations will be generated. This can of
     * course quickly create a number of passwords which cannot be searched on
     * a single machine in a reasonable amount of time.
     */
    public static class GeneratorSource implements Source {
        /** The built-in in sets. */
        public enum Set {
            /** Numbers only (0..9). */
            NUMBERS,
            /** Numbers and letters (A..Z, lower and upper cases).*/
            NUMBERS_LETTERS,
            /** All characters from 32 (space) to 126 ASCII. */
            PRINTABLE_ASCII,
            /** All characters from 32 (space) to 255 ASCII. */
            SPACE_TO_255
        }
        /**
         * Constructor to use a predefined set of characters.
         * @param len The length of the passwords (minimum is 1).
         * @param set The set to use.
         */
        public GeneratorSource(int len, Set set) {
            char[] s = null;
            switch(set) {
            case NUMBERS:
                s = "0123456789".toCharArray();
                break;
            case NUMBERS_LETTERS:
                s = ("abcdefghijklmnopqrstuvwxyz0123456789" +
                     "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
                break;
            case PRINTABLE_ASCII:
                s = new char[127 - ' '];
                for (int c = ' '; c < 127; c++) {
                    s[c - ' '] = (char)c;
                }
                break;
            case SPACE_TO_255:
                s = new char[256 - ' '];
                for (int c = ' '; c < 256; c++) {
                    s[c - ' '] = (char)c;
                }
                break;
            }
            init(len, s);
        }
        /**
         * Constructor for a user-defined set of characters.
         * @param len The length of the passwords (minimum is 1).
         * @param set The user-defined set (at least once character long).
         */
        public GeneratorSource(int len, char[] set) {
            init(len, set);
        }
        void init(int len, char[] set) {
            if (0 == len || 0 == set.length) {
                return;
            }
            this.buf = new char[len];
            this.set = set;
            this.c = new int[this.buf.length];
        }
        @Override
        public boolean hasNext() {
            return null != this.c;
        }
        @Override
        public String next() {
            for (int i = 0; i < this.c.length; i++) {
                this.buf[i] = this.set[this.c[i]];
            }
            for (int i = this.c.length - 1; i >= 0; i--) {
                if (++this.c[i] >= this.set.length) {
                    if (0 == i) {
                        this.c = null;
                        break;
                    }
                    this.c[i] = 0;
                    continue;
                }
                break;
            }
            return new String(this.buf);
        }
        @Override
        public void remove() {
        }
        @Override
        public void close() {
        }
        char[] buf;
        char[] set;
        int[] c;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    final ExecutorService         exsvc;
    final Source                  src;
    final byte[]                  header;
    final AtomicLong              progress = new AtomicLong();
    final AtomicReference<String> found = new AtomicReference<String>();
    
    /** 
     * Each core calls this code. All of the state is on the stack or made
     * thread-safe, hence the instance can be shared between all threads.
     */
    final Runnable run = new Runnable() {
        @Override
        public void run() {
            final PasswordSearch self = PasswordSearch.this;
            byte[] hdr = self.header.clone();
            while (null == PasswordSearch.this.found.get()) {
                String pw;
                synchronized(PasswordSearch.this.src) {
                    if (!self.src.hasNext()) {
                        return;
                    }
                    pw = PasswordSearch.this.src.next();
                }
                try {
                    if ("a_1".equals(pw)) {
                        pw = "a_1";
                    }
                    new Header(new Password(pw.toCharArray(), null), hdr, 0);
                    self.found.set(pw);
                    return;
                }
                catch (NoMatchingAlgorithmException nmae) {
                    self.progress.incrementAndGet();
                    System.arraycopy(self.header, 0, hdr, 0, 512);
                }
                catch (TCLibException tle) {
                    tle.printStackTrace(System.err);
                    return;
                }
            }
        }
    };
    
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     * @param src The password source.
     * @param volume The file to look out for passwords.
     * @throws IOException If the file cannot be opened or read.
     */
    public PasswordSearch(Source src, File volume) throws IOException {
        this.header = new byte[Header.BLOCK_SIZE * 
                               Header.BLOCK_COUNT];
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(volume, "r");
            raf.readFully(this.header, 0, Header.BLOCK_SIZE);
        }
        finally {
            if (null != raf) {
                raf.close();
            }
        }
        this.src = src;
        int pcount = Runtime.getRuntime().availableProcessors();
        this.exsvc = Executors.newFixedThreadPool(pcount);
        for (int i = 0; i < pcount; i++) {
            this.exsvc.execute(this.run);
        }
    }

    /**
     * @return The search progress, which is number of passwords tried so far.
     */
    public long progress() {
        return this.progress.get();
    }

    /**
     * @return The found password or null if none has been found (yet).
     */
    public String found() {
        return this.found.get();
    }

    /**
     * Cleans up resources of this instance. Discard after this call is made.
     */
    public void end() {
        this.exsvc.shutdown();
        this.src.close();
    }

    ///////////////////////////////////////////////////////////////////////////

    public static ExitCode _main(String[] args) {
        if (3 > args.length) {
            System.out.println(
                "USAGE: tcpws [volume] [source] [param] {param} ...\n" +
                "sources:\n" +
                "    file [filename]\n" +           
                "    generator [length] [type] {param}\n" +           
                "generators:");
            for (GeneratorSource.Set gset : GeneratorSource.Set.values()) {
                System.out.println("    " + gset.name().toLowerCase());
            }
            System.out.println("    user_defined [characters]\n" +
                "EXAMPLES:\n" +
                "tcpws test.tc file passwords.txt\n" +     
                "tcpws test.tc generator 5 numbers_letters\n" +    
                "tcpws test.tc generator 10 user_defined abcdef123!_\n");    
            return ExitCode.WRONG_ARGS;
        }
        File vol = new File(args[0]);
        Source src;
        if (args[1].equals("file")) {
            try {
                src = new StreamSource(
                      new BufferedInputStream(
                      new FileInputStream(args[2])));
            }
            catch (IOException ioe) {
                System.err.println(ioe.getMessage());
                return ExitCode.IO_ERROR;
            }
        }
        else if (args[1].equals("generator")) {
            if (4 > args.length) {
                System.err.println("not enough parameters");
                return ExitCode.WRONG_ARGS;
            }
            int len;
            try {
                len = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException nfe) {
                System.err.printf("invalid length (%s)", nfe.getMessage());
                return ExitCode.WRONG_ARGS;
            }
            try {
                GeneratorSource.Set gset = GeneratorSource.Set.valueOf(args[3].toUpperCase());
                src = new GeneratorSource(len, gset);
            }
            catch (IllegalArgumentException iae) {
                if ("user_defined".equalsIgnoreCase(args[3])) {
                    if (5 > args.length) {
                        System.err.println("missing user define characters");
                        return ExitCode.WRONG_ARGS;
                    }
                    src = new GeneratorSource(len, args[4].toCharArray());
                }
                else {
                    System.err.println("unknown generator '" + args[2] + "'");
                    return ExitCode.WRONG_ARGS;
                }
            }
        }        
        else {
            System.err.println("unknown source '" + args[1] +"'");
            return ExitCode.WRONG_ARGS;
        }
        try {
            PasswordSearch ps = new PasswordSearch(src, vol);
            while (null == ps.found()) {
                synchronized(src) {
                    if (!src.hasNext()) {
                        break;
                    }
                }
                System.out.printf("%,d passwords tested\r", ps.progress());
                Thread.sleep(1000);
            }
            ps.end();
            if (null == ps.found()) {
                System.out.println("sorry, no password was found");
                return ExitCode.NOT_FOUND;
            }
            else {
                System.out.printf("password found: >>>%s<<<\n", ps.found());
                return ExitCode.SUCCESS;
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return ExitCode.ERROR;
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        System.exit(_main(args).ordinal());
    }
}
