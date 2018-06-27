package com.kh.keyboard;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CSIICypher implements Cypher {
    private String degree = "S";
    private int rc4keyLength = 8;
    private int rc4FakeDataLength = 512;
    private static Map plainMap;
    private static Provider scProvider = Security.getProvider("SC");
    private static String modulus_hsm="9f085f4a8c9ca5c02c0cda21638a65875494781469a736745c3da96536fc7d1171332ce42e58324825b63da0ccb29b2be9f962460b04924b782f872d73152be5a7f2c616a18d2c225ca2291a4fcaf80d5e1d2e608f31e35f8ff2a1119763289cc541922ff6f242c93c5eb766d0ece309833e8c6bbfab579c481c0cb39670465ae2321c1d2785afcddccdeb5a954329854e8c3cbe65a873c455018e74b9933791fdda357d1f1c00999cab721ace88059e578e1ca1271ee4168e39ac79b9309b318fbf86f395f062dcf84df60a23145c2cd085eb8dd40542076ac0ed5ec4d4a1b70682d1798659822a7a8648078a8c58a1b638189da22b92b74f3104f585350f41";

    static {
        if (scProvider == null) {
            Security.addProvider(new BouncyCastleProvider());
            scProvider = Security.getProvider("SC");
            if (scProvider == null) {
                Log.e("", "cannot find SC JCE Providor");
            }
        }

        plainMap = new HashMap();
    }

    public CSIICypher() {
    }

    public static CSIICypher newInstance() {
        return new CSIICypher();
    }

    public String checkLevel(String name) {
        if (name != null && !name.trim().equals("") && plainMap != null) {
            String plain = (String) plainMap.get(name);
            if (plain == null) {
                return "W";
            } else {
                Pattern p = Pattern.compile("^((.)*[a-zA-Z]+(.)*[0-9]+(.)*)|((.)*[0-9]+(.)*[a-zA-Z]+(.)*)$");
                Matcher m = p.matcher(plain);
                return m.find() ? "S" : "W";
            }
        } else {
            return "W";
        }
    }

    public int getPasswordLength(String name) {
        if (name != null && !name.trim().equals("") && plainMap != null) {
            String plain = (String) plainMap.get(name);
            return plain == null ? 0 : plain.length();
        } else {
            return 0;
        }
    }

    public void deleteLastPwdChar(String name) {
        this.putChar(name, "delete");
    }

    public synchronized void putChar(String name, String str) {
        if (name != null && !name.trim().equals("") && str != null && !str.equals("")) {
            String plain = (String) plainMap.get(name);
            if (plain == null) {
                plain = "";
                plainMap.put(name, plain);
            }

            if (!str.equals("ok")) {
                if (str.equals("delete")) {
                    if (plain.length() <= 1) {
                        plain = "";
                    } else {
                        plain = plain.substring(0, plain.length() - 1);
                    }
                } else if (str.equals("clear")) {
                    plain = "";
                } else {
                    plain = plain + str;
                }

                plainMap.put(name, plain);
            }
        }
    }

    public synchronized void clearChar(String name) {
        plainMap.put(name, "");
    }

    public String encrypt(String plainorname, String modulus, String timestamp, String encoding, int flag) throws SecurityCypherException {
        String res = this.encryptWithoutRemove(plainorname, modulus, timestamp, encoding, flag);
        if (flag == 1) {
            plainMap.remove(plainorname);
        }
        return res;
    }

    private String readModulus_hsm() throws SecurityCypherException {
        //check
        //....

        return modulus_hsm;
    }

    private String readModulus(String modulus) throws SecurityCypherException {
        //check
        //....

//        return modulus;
        return modulus.substring(32);
    }

    public String encryptWithoutRemove(String plainorname, String modulus, String timestamp, String encoding, int flag) throws SecurityCypherException {
        String res = null;
        String plain = plainorname;
        if (flag == 1) {
            if (plainorname == null) {
                throw new SecurityCypherException("password_name_is_null");
            }

            plain = (String) plainMap.get(plainorname);
        }

        if (modulus == null) {
            throw new SecurityCypherException("modulus_is_null");
        } else if (plain == null) {
            throw new SecurityCypherException("plain_is_null");
        } else if (timestamp == null) {
            throw new SecurityCypherException("timestamp_is_null");
        } else {
            try {
                byte[] enc_rsa1_data = this.rsaEncrypt(plain.getBytes(), readModulus_hsm());

                StringBuilder sb=new StringBuilder();
                for (byte b:enc_rsa1_data){
                    sb.append(String.format("%02x",b));
                }
                byte[] pln_rsa2_data =
                        new StringBuilder(this.degree).append(timestamp).append("_").append(sb.toString()).toString().getBytes("iso8859-1");
                byte[] desKey = new byte[this.rc4keyLength];
                SecureRandom secRan = new SecureRandom();
                secRan.nextBytes(desKey);
                byte[] encDataBytes = this.encrypt3DES(pln_rsa2_data, desKey);

                byte[] encKeyBytes = this.rsaEncrypt(desKey, readModulus(modulus));
//                byte[] inversersacyphered = this.inverseBytes(rc4keycyphered); wudongrui
                byte[] finaldata = this.generateFinal(encKeyBytes, encDataBytes);

                res = Base64.encodeToString(finaldata, 2);
                return res;
            } catch (Exception var16) {
                throw new SecurityCypherException(var16);
            }
        }
    }
    public String  encryptWithJiamiJi(String plainorname, String dbp,String hms, String timestamp, String encoding, int flag) throws SecurityCypherException {
        String res = null;
        String plain = plainorname;
        if (flag == 1) {
            if (plainorname == null) {
                throw new SecurityCypherException("password_name_is_null");
            }

            plain = (String) plainMap.get(plainorname);
        }

        if (dbp == null) {
            throw new SecurityCypherException("modulus_is_null");
        } else if (plain == null) {
            throw new SecurityCypherException("plain_is_null");
        } else if (timestamp == null) {
            throw new SecurityCypherException("timestamp_is_null");
        } else {
            try {
                byte[] enc_rsa1_data = this.rsaEncrypt(plain.getBytes(),hms);

                StringBuilder sb=new StringBuilder();
                for (byte b:enc_rsa1_data){
                    sb.append(String.format("%02x",b));
                }
                byte[] pln_rsa2_data =
                        new StringBuilder(this.degree).append(timestamp).append("_").append(sb.toString()).toString().getBytes("iso8859-1");
                byte[] desKey = new byte[this.rc4keyLength];
                SecureRandom secRan = new SecureRandom();
                secRan.nextBytes(desKey);
                byte[] encDataBytes = this.encrypt3DES(pln_rsa2_data, desKey);

                byte[] encKeyBytes = this.rsaEncrypt(desKey, dbp.substring(32));
//                byte[] inversersacyphered = this.inverseBytes(rc4keycyphered); wudongrui
                byte[] finaldata = this.generateFinal(encKeyBytes, encDataBytes);

                res = Base64.encodeToString(finaldata, 2);
                return res;
            } catch (Exception var16) {
                throw new SecurityCypherException(var16);
            }
        }
    }
    public byte[] encrypt3DES(final byte[] src, final byte[] key) throws Exception {

        SecretKeySpec deskey= new SecretKeySpec(key, "DESede");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        return cipher.doFinal(src);

    }



    /**
     * @param plaintext 明文
     * @param modulus   公钥
     * @param timestamp 时间戳
     * @param encoding  编码格式
     * @param flag
     * @return
     * @throws SecurityCypherException
     */
    public String csiiEncryptPlainText(String plaintext, String modulus, String timestamp, String encoding, int flag) throws SecurityCypherException {
        String res = null;
        if (modulus == null) {
            throw new SecurityCypherException("modulus_is_null");
        } else if (plaintext == null) {
            throw new SecurityCypherException("plain_is_null");
        } else if (timestamp == null) {
            throw new SecurityCypherException("timestamp_is_null");
        } else {
            try {
                String e = this.degree + timestamp + ":" + plaintext;
                byte[] plainBytes = e.getBytes(encoding);
                byte[] rc4key = new byte[this.rc4keyLength];
                SecureRandom secRan = new SecureRandom();
                secRan.nextBytes(rc4key);
                byte[] rc4cyphered = this.rc4Encrypt(plainBytes, rc4key);
                byte[] rc4keycyphered = this.rsaEncrypt(rc4key, modulus);
                byte[] inversersacyphered = this.inverseBytes(rc4keycyphered);
                byte[] finaldata = this.generateFinal(inversersacyphered, rc4cyphered);
                res = Base64.encodeToString(finaldata, 2);
                return res;
            } catch (Exception var16) {
                throw new SecurityCypherException(var16);
            }
        }
    }



    private byte[] rc4Encrypt(byte[] plain, byte[] key) throws SecurityCypherException {
        try {
            Cipher exception = Cipher.getInstance("RC4", scProvider);
            SecretKeySpec tmpKey = new SecretKeySpec(key, "RC4");
            exception.init(1, tmpKey);
            byte[] fakeDataIn = new byte[this.rc4FakeDataLength];
            byte[] fakeDataOut = new byte[this.rc4FakeDataLength];
            exception.update(fakeDataIn);
            byte[] dest = exception.doFinal(plain);
            return dest;
        } catch (Exception var8) {
            throw new SecurityCypherException(var8);
        }
    }

    private byte[] rsaEncrypt(byte[] plain, String modulus) throws SecurityCypherException {
        try {
            Cipher exception = Cipher.getInstance("RSA/ECB/PKCS1Padding", scProvider);
            PublicKey key = this.getPublicKey(modulus, "10001");
            exception.init(1, key);
            byte[] dest = exception.doFinal(plain);
            return dest;
        } catch (Exception var6) {
            throw new SecurityCypherException(var6);
        }
    }

    private PublicKey getPublicKey(String modulus, String exp) throws SecurityCypherException {
        try {
            BigInteger exception = new BigInteger(modulus, 16);
            BigInteger e = new BigInteger(exp, 16);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(exception, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception var8) {
            throw new SecurityCypherException(var8);
        }
    }

    private byte[] inverseBytes(byte[] org) {
        byte[] dest = new byte[org.length];

        for (int i = 0; i < org.length; ++i) {
            dest[i] = org[org.length - 1 - i];
        }

        return dest;
    }

    private byte[] generateFinal(byte[] part1, byte[] part2) throws SecurityCypherException {
        try {
            int exception = 20 + part1.length + 8 + part2.length;
            byte[] finalbytes = new byte[exception];
            String part1len = String.format("%1$08d", new Object[]{Integer.valueOf(12 + part1.length)});
            byte[] part1bytes = part1len.getBytes("utf-8");
            String part2len = String.format("%1$08d", new Object[]{Integer.valueOf(part2.length)});
            byte[] part2bytes = part2len.getBytes("utf-8");
            int k = 0;

            int i;
            for (i = 0; i < 8; ++i) {
                finalbytes[k++] = part1bytes[i];
            }

            finalbytes[k++] = 0x57; //1
            finalbytes[k++] = 0x00; //2
            finalbytes[k++] = 0;
            finalbytes[k++] = 0;
            finalbytes[k++] = 1;
            finalbytes[k++] = 104;
            finalbytes[k++] = 0;
            finalbytes[k++] = 0;
            finalbytes[k++] = 0;
            finalbytes[k++] = -92;
            finalbytes[k++] = 0;
            finalbytes[k++] = 0;

            for (i = 0; i < part1.length; ++i) {
                finalbytes[k++] = part1[i];
            }

            for (i = 0; i < 8; ++i) {
                finalbytes[k++] = part2bytes[i];
            }

            for (i = 0; i < part2.length; ++i) {
                finalbytes[k++] = part2[i];
            }
//            String fb=new String(finalbytes);
//            StringBuilder sb=new StringBuilder(fb);
//            insertStringInParticularPosition(fb.trim(),"WW",9);
            return finalbytes;
        } catch (Exception var11) {
            throw new SecurityCypherException(var11);
        }
    }
    public String insertStringInParticularPosition(String src, String dec, int position){
        StringBuffer stringBuffer = new StringBuffer(src);
        return stringBuffer.insert(position, dec).toString();
    }


    public String encryptCommon(String plain, String modulus) throws Exception {
        String plainValue = (String) plainMap.get(plain);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey key = this.getPublicKey(modulus, "10001");
        cipher.init(1, key);
        byte[] dest = cipher.doFinal(plainValue.getBytes("UTF-8"));
        return Base64.encodeToString(dest, 2);
    }

}
