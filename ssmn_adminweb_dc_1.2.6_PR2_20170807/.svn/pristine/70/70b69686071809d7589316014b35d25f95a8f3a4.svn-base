/*
 * Created on 2006-7-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dascom.OPadmin.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author RubinRuler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Md5PasswordEncoder extends BaseDigestPasswordEncoder implements PasswordEncoder {

	/**
	 * 
	 */
	public Md5PasswordEncoder() {
		super();
	}

    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);

        return pass1.equals(pass2);
    }

    public String encodePassword(String rawPass, Object salt) {
        String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

        if (!getEncodeHashAsBase64()) {
            return DigestUtils.md5Hex(saltedPass);
        }
        
        byte[] encoded = Base64.encodeBase64(DigestUtils.md5(saltedPass));
        return new String(encoded);
    }
    
    public static void main(String[] args){
    	Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    	String a = md5.encodePassword("460013659086695", null);
    	System.out.println(a);
    }
}
