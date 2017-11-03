/*
 * Created on 2006-7-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dascom.OPadmin.util;

/**
 * @author RubinRuler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class BaseDigestPasswordEncoder extends BasePasswordEncoder {
    private boolean encodeHashAsBase64 = false;

    //~ Methods ================================================================

    /**
     * The encoded password is normally returned as Hex (32 char) version of
     * the hash bytes. Setting this property to true will cause the encoded
     * pass to be returned as Base64 text, which will consume 24 characters.
     *
     * @param encodeHashAsBase64 set to true for Base64 output
     */
    public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
        this.encodeHashAsBase64 = encodeHashAsBase64;
    }

    public boolean getEncodeHashAsBase64() {
        return encodeHashAsBase64;
    }
}
