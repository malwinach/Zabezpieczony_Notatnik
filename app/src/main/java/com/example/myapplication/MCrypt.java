//package com.example.myapplication;
//
//import java.security.NoSuchAlgorithmException;
//
//import javax.crypto.Cipher;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//
//public class MCrypt {
//
//    private static String iv;
//    private static String SecretKey;
//    private static IvParameterSpec ivspec;
//    private static SecretKeySpec keyspec;
//    private static Cipher cipher;
//
//
//    public MCrypt() {
//
//        try {
//            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void setKeys() {
//        //byte[] iv = Base64.decode(Open(ivFilename), Base64.DEFAULT);
//        /*
//        try {
//            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyStore.load(null);
//            if (!keyStore.containsAlias(KEY_ALIAS)) {
//                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//
//                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
//                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//                        .build());
//
//                return keyGenerator.generateKey();
//            } else {
//                System.out.println(keyStore.getKey(KEY_ALIAS, null).toString());
//                return keyStore.getKey(KEY_ALIAS, null);
//            }
//        }
//        catch (KeyStoreException e) {
//            Toast.makeText(this, "KEY STORE EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (CertificateException e) {
//            Toast.makeText(this, "CERTIFICATE EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (IOException e) {
//            Toast.makeText(this, "IO EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (NoSuchAlgorithmException e) {
//            Toast.makeText(this, "NO SUCH ALGORITHM EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (NoSuchProviderException e) {
//            Toast.makeText(this, "NO SUCH PROVIDER EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (InvalidAlgorithmParameterException e) {
//            Toast.makeText(this, "INVALID ALGORITHM EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//        catch (UnrecoverableKeyException e) {
//            Toast.makeText(this, "UNRECOVERABLE KEY EXCEPTION", Toast.LENGTH_SHORT).show();
//        }
//         */
//        iv = MainActivity.hashKey.substring(5,21);
//        SecretKey =  MainActivity.hashKey.substring(6,38);
//        ivspec = new IvParameterSpec(iv.getBytes());
//        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
//    }
//
//    public static byte[] encrypt(String text) throws Exception {
//        setKeys();
//        if (text == null || text.length() == 0)
//            throw new Exception("Empty string");
//
//        byte[] encrypted = null;
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//
//            encrypted = cipher.doFinal(padString(text).getBytes());
//        } catch (Exception e) {
//            throw new Exception("[encrypt] " + e.getMessage());
//        }
//        return encrypted;
//    }
//
//    public static byte[] decrypt(String text) throws Exception {
//        setKeys();
//        if (text == null || text.length() == 0)
//            throw new Exception("Empty string");
//
//        byte[] decrypted = null;
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//
//            decrypted = cipher.doFinal(hexToBytes(text));
//        } catch (Exception e) {
//            throw new Exception("[decrypt] " + e.getMessage());
//        }
//        return decrypted;
//    }
//
//    public static String byteArrayToHexString(byte[] array) {
//        StringBuffer hexString = new StringBuffer();
//        for (byte b : array) {
//            int intVal = b & 0xff;
//            if (intVal < 0x10)
//                hexString.append("0");
//            hexString.append(Integer.toHexString(intVal));
//        }
//        return hexString.toString();
//    }
//
//    public static byte[] hexToBytes(String str) {
//        if (str == null) {
//            return null;
//        } else if (str.length() < 2) {
//            return null;
//        } else {
//
//            int len = str.length() / 2;
//            byte[] buffer = new byte[len];
//            for (int i = 0; i < len; i++) {
//                buffer[i] = (byte) Integer.parseInt(
//                        str.substring(i * 2, i * 2 + 2), 16);
//
//            }
//            return buffer;
//        }
//    }
//
//    private static String padString(String source) {
//        char paddingChar = 0;
//        int size = 16;
//        int x = source.length() % size;
//        int padLength = size - x;
//        for (int i = 0; i < padLength; i++) {
//            source += paddingChar;
//        }
//        return source;
//    }
//
//    public static String HexToASCII(String hex) {
//
//        if(hex.length()%2!=0){
//            System.err.println("Invlid hex string.");
//        }
//
//        StringBuilder builder = new StringBuilder();
//
//        for (int i = 0; i < hex.length(); i = i + 2) {
//            // Step-1 Split the hex string into two character group
//            String s = hex.substring(i, i + 2);
//            // Step-2 Convert the each character group into integer using valueOf method
//            int n = Integer.valueOf(s, 16);
//            // Step-3 Cast the integer value to char
//            builder.append((char)n);
//        }
//
//        return builder.toString();
//    }
//}
