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

import java.util.HashMap;

import coderslagoon.baselib.util.BytePtr;

public class CharacterSet {
    public enum Type {
        CS0(0),
        CS1(1),
        CS2(2),
        CS3(3),
        CS4(4),
        CS5(5),
        CS6(6),
        CS7(7),
        CS8(8);
        
        Type(int code) {
            this.code = code;
        }

        public final int code;
        
        static HashMap<Integer, Type> _codeMap = new HashMap<Integer, Type>();
        static {
            for (Type t : values()) {
                _codeMap.put(t.code, t);
            }
        }

        public static Type fromCode(int code) {
            return _codeMap.get(code);
        }
    }
    
    public final static int LENGTH = 64;
    public final static int INFORMATION_LENGTH = LENGTH - 1;
    
    public final static CharacterSet OSTA_COMPRESSED_UNICODE = 
                        CharacterSet.create(Type.CS0, "OSTA Compressed Unicode");  
    
    public Type    type;
    public BytePtr information;
    
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof CharacterSet) {
    		CharacterSet cs = (CharacterSet)obj;
    		
    		return cs.type == this.type && 
    		       cs.information.equals(this.information);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return this.type       .hashCode() ^ 
    	       this.information.hashCode();
    }
    
    public static CharacterSet parse(byte[] buf, int ofs) throws UDFException {
        int t = buf[ofs++] & 0x0ff;
        
        CharacterSet result = new CharacterSet();
        
        if (null == (result.type = Type.fromCode(t))) {
            throw new UDFException("unknown character set %d", t);   
        }
        
        result.information = new BytePtr.Checked(buf, ofs, INFORMATION_LENGTH);
        
        //result.information.grab();
        //BinUtils.hexDump(result.information.buf, System.out, 16, 4); 
        
        return result;
    }

    public static CharacterSet create(Type type, String information) {
        CharacterSet result = new CharacterSet();
        
        result.type        = type;
        result.information = new BytePtr(new byte[INFORMATION_LENGTH]);
        
        byte[] inf = information.getBytes();

        System.arraycopy(inf, 0, 
                result.information.buf,
                result.information.ofs,
                inf.length);
        
        return result;
    }
    
    public void write(byte[] buf, int ofs) {
        buf[ofs++] = (byte)this.type.code;
        System.arraycopy(this.information.buf,
                         this.information.ofs,
                         buf,
                         ofs,
                         this.information.len);
    }
    
    public String toString() {
        return String.format("CSET:type=%s", this.type);    
    }
}
