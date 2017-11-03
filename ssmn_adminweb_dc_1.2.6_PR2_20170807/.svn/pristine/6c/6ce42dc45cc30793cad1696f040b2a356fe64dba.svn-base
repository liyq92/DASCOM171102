package com.dascom.OPadmin.action;

/**
 * 类功能:数据的加密和解密
 */
 
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class DesEncryptUtil{
    private Key key;
 
    public DesEncryptUtil() {
        this.getKey("jiangfeng");
    }
 
    /**
     * 根据参数生成KEY
     * 
     * @param strKey
     */
    public void getKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(strKey.getBytes()));
            this.key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
 
    /**
     * 加密String明文输入,String密文输出
     * 
     * @param strMing
     * @return
     */
    public String getEncString(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byteMing = strMing.getBytes("UTF-8");
            byteMi = this.getEncCode(byteMing);
            strMi = base64en.encode(byteMi);
        } catch (Exception e) {
           // e.printStackTrace();
        } finally {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }
 
    /**
     * 解密 以String密文输入,String明文输出
     * 
     * @param strMi
     * @return
     */
    public String getDesString(String strMi) {
        BASE64Decoder base64De = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decodeBuffer(strMi);
            byteMing = this.getDesCode(byteMi);
            strMing = new String(byteMing, "UTF-8");
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }
 
    /**
     * 加密以byte[]明文输入,byte[]密文输出
     * 
     * @param byteS
     * @return
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }
 
    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     * 
     * @param byteD
     * @return
     */
    private byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }
     
    public static void main(String args[]) {
        DesEncryptUtil des = new DesEncryptUtil();
        // 实例化一个对像
        String strEnc = des
                .getEncString("000000");
        //加密字符串,返回String的密文
        System.out.println("加密文:" + strEnc);
        String strDes = des.getDesString(strEnc);// 把String 类型的密文解密
        System.out.println("解密文:" + strDes);
    }
    
}

