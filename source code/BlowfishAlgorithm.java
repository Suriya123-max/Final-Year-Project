/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author welcome
 */
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author dhanoopbhaskar
 */
public class BlowfishAlgorithm {

    KeyGenerator keyGenerator = null;
    SecretKey secretKey = null;
    Cipher cipher = null;

    public BlowfishAlgorithm() {
        try {
            /**
             * Create a Blowfish key
             */
            keyGenerator = KeyGenerator.getInstance("Blowfish");
            secretKey = keyGenerator.generateKey();

            /**
             * Create an instance of cipher mentioning the name of algorithm
             *     - Blowfish
             */
            cipher = Cipher.getInstance("Blowfish");
        } catch (NoSuchPaddingException ex) {
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
        }

    }

    /**
     *
     * @param plainText
     * @return cipherBytes
     */
    public byte[] encryptText(String plainText) {
        byte[] cipherBytes = null;
        try {
            /**
             * Initialize the cipher for encryption
             */
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            /**
             * Convert the text string to byte format
             */
            byte[] plainBytes = plainText.getBytes();
            /**
             * Perform encryption with method doFinal()
             */
            cipherBytes = cipher.doFinal(plainBytes);
        } catch (IllegalBlockSizeException ex) {
            System.out.println(ex);
        } catch (BadPaddingException ex) {
            System.out.println(ex);
        } catch (InvalidKeyException ex) {
            System.out.println(ex);
        }

        return cipherBytes;
    }

    /**
     *
     * @param cipherBytes
     * @return plainText
     */
    public String decryptText(byte[] cipherBytes) {
        String plainText = null;
        try {
            /**
             * Initialize the cipher for decryption
             */
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            /**
             * Perform decryption with method doFinal()
             */
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            /**
             * Convert encrypted text to string format
             */
            plainText = new String(plainBytes);
        } catch (IllegalBlockSizeException ex) {
            System.out.println(ex);
        } catch (BadPaddingException ex) {
            System.out.println(ex);
        } catch (InvalidKeyException ex) {
            System.out.println(ex);
        }

        return plainText;
    }

    /**
     *
     * @param plainText
     * @return cipherText
     */
    public String encrypt(String plainText) {
        String cipherText = null;
        byte[] cipherBytes = encryptText(plainText);
        cipherText = bytesToString(cipherBytes);
        return cipherText;
    }

    /**
     * 
     * @param cipherText
     * @return plainText
     */
    public String decrypt(String cipherText) {
        String plainText = null;
        byte[] cipherBytes = stringToBytes(cipherText);
        plainText = decryptText(cipherBytes);
        return plainText;
    }

//    public static void main(String[] args) {
//        BlowfishAlgorithm blowfishAlgorithm = new BlowfishAlgorithm();
//        String textToEncrypt = "Blowfish Algorithm";
//        System.out.println("Text before Encryption: " + textToEncrypt);
//        String cipherText = blowfishAlgorithm.encrypt(textToEncrypt);
//        System.out.println("Cipher Text: " + cipherText);
//        System.out.println("Text after Decryption: " + blowfishAlgorithm.decrypt(cipherText));
//    }

    /**
     * 
     * @param rawText
     * @return plainText
     *
     * Perform Base64 encoding
     */
    private String bytesToString(byte[] rawText) {
        String plainText = null;
        plainText = Base64.encode(rawText);
        return plainText;
    }

    /**
     * 
     * @param plainText
     * @return rawText
     *
     * Perform Base64 decoding
     */
    private byte[] stringToBytes(String plainText) {
        byte[] rawText = null;
        try {
            rawText = Base64.decode(plainText);
        } catch (Base64DecodingException ex) {
            System.out.println(ex);
        }
        return rawText;
    }
}

