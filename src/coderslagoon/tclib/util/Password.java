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

package coderslagoon.tclib.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

/**
 * To safely convert passwords into a binary key. This has to do with the fact
 * that TrueCrypt chooses a certain character set, so to stay compatible with
 * characters beyond the ASCII space one has to apply the same transformation.
 */
public class Password extends Key.ByteArray {
    /**
     * Default ctor.
     * @param passw The password to convert.
     * @param charsetName The character set name. Can be null to choose the
     * default one (which TrueCrypt uses).
     * @throws TCLibException If any error occurred.
     */
    public Password(char[] passw, String charsetName) throws TCLibException {
        super();
        try {
            this.data = init(
                    passw,
                    null == charsetName ? "ISO-8859-1" : charsetName);
        }
        finally {
            Arrays.fill(passw, '\0');
        }
    }

    static byte[] init(char[] passw, String charsetName) throws TCLibException {
        final Charset cs;

        if (null == charsetName) {
            cs = Charset.defaultCharset();
        }
        else {
            if (null == (cs = Charset.forName(charsetName))) {
                throw new TCLibException("no such character set");
            }
        }

        final CharsetEncoder enc = cs.newEncoder();

        final CharBuffer cbuf = CharBuffer.allocate(passw.length);
        for (char c : passw) {
            cbuf.append(c);
            c = '\0';
        }
        cbuf.flip();

        ByteBuffer bbuf = null;
        try {
            bbuf = enc.encode(cbuf);
        }
        catch (CharacterCodingException cce) {
            throw new TCLibException(cce);
        }
        finally {
            char[] cbufarr = cbuf.array();
            Arrays.fill(cbufarr, (char)0);
        }

        final byte[] result = new byte[bbuf.remaining()];

        final byte[] bbufarr = bbuf.get(result, 0, result.length).array();
        Arrays.fill(bbufarr, (byte)0);

        return result;
    }
}
