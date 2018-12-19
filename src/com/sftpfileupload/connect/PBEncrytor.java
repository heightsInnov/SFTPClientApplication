package com.sftpfileupload.connect;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.security.InvalidKeyException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import java.security.spec.InvalidKeySpecException;

public class PBEncrytor {
    String passPhrase0 = "***MyRedeemerLives7";

    public PBEncrytor () {

    }
    public String PBEncrypt(String str) {
      return PBEncrypt(str,passPhrase0);
    }

    public String PBDecrypt(String str) {
     return PBDecrypt(str,passPhrase0);
    }
//
    
    public static void main(String[] args) {
        PBEncrytor Pb = new PBEncrytor ();
    //    5WCOkudGCczIVi2JAnfFFA==
      //// String dddd=  Pb.PBEncrypt("ncsmagic1234_","***MyRedeemerLives007");
//    String sip=  Pb.PBEncrypt("10.8.0.55");
//	String suser=  Pb.PBEncrypt("oradebug");
//	String spass=  Pb.PBEncrypt("debug123");
    //String defizer=Pb.PBDecrypt("Password1$","***MyRedeemerLives7");
   // String defizer = Pb.PBDecrypt("eWlMEd1F/qc2nx3cKdyZKC2UfRH0WR3T");
//    System.out.println("source >>>> "+sip+" ---- "+suser+" ----- "+spass);
//    String defizer = Pb.PBDecrypt("8PtIBGY1oyO2AnBHci8olA==");
//    System.out.println("defizer>>>>"+defizer);
    }
    public String PBEncrypt(String str,String passPhrase) {
        Cipher ecipher;
        Cipher dcipher;

        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };

        int iterationCount = 19;

        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            if(str!=null){
                byte[] utf8 = str.getBytes("UTF8");
                byte[] enc = ecipher.doFinal(utf8);
                return new sun.misc.BASE64Encoder().encode(enc);
            }
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        } catch (Exception ee){;}

        return null;
    }

    public String PBDecrypt(String str,String passPhrase) {
        Cipher ecipher;
        Cipher dcipher;

        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };

        int iterationCount = 19;

        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            if(str!=null){
                byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
                byte[] utf8 = dcipher.doFinal(dec);
                return new String(utf8, "UTF8");
            }
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }catch (Exception ee){;}

        return null;
    }
}