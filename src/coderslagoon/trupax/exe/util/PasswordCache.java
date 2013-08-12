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

package coderslagoon.trupax.exe.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordCache {

	private final static String CHARSET = "UTF-8";
    private byte[] otp, bpassw;

    public void set(String passw) {
    	clear();
    	try {
    		this.bpassw = passw.getBytes(CHARSET);
    	}
    	catch (UnsupportedEncodingException usee) {
    		throw new Error(usee);
    	}
    	this.otp = new byte[this.bpassw.length];
    	new SecureRandom().nextBytes(this.otp);
    	for (int i = 0; i < this.otp.length; i++) {
    		this.bpassw[i] ^= this.otp[i];
    	}
    }
    
    public String get() {
    	String result = null;
    	if (null != this.otp) {
    		for (boolean dec : new boolean[] { true, false }) {
	        	for (int i = 0; i < this.otp.length; i++) {
	        		this.bpassw[i] ^= this.otp[i];
	        	}
	        	if (dec) {
	        		result = new String(this.bpassw, Charset.forName(CHARSET));
	        	}
    		}
    	}
    	return result;
    }
    
    public void clear() {
    	if (null != this.bpassw) {
    		for (int i = 255; i >= 0; i--) {
		    	Arrays.fill(this.otp   , (byte)i);
		    	Arrays.fill(this.bpassw, (byte)i);
    		}
	    	this.otp    = null;
	    	this.bpassw = null;
    	}
    }
    
    @Override
    public void finalize() throws Throwable {
    	clear();
    	super.finalize();
    }
}
