package rsa;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Utility class for RSA related utility functions.
 */
public class RSAUtil {
	
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	
	/**
	 * Generates a new set of public and private keys. Should be used only once.<br/>
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void generateKeys(String keysDir) throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
		keyGen.initialize(512);
		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keysDir + "/private.key"));
		oos.writeObject(privateKey);
		oos.close();
		
		oos = new ObjectOutputStream(new FileOutputStream(keysDir + "/public.key"));
		oos.writeObject(publicKey);
		oos.close();
	}
	
	/**
	 * Use this main method to get a pin encrypted.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String pin = "vince";
		System.out.println(encryptToHex(pin));
	}
	
	/**
	 * Loads the PublicKey from public.key
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey() throws Exception {
		if (publicKey == null) {
			ObjectInputStream ois = new ObjectInputStream(RSAUtil.class.getClassLoader().getResourceAsStream("rsa/public.key"));
			publicKey = (PublicKey)ois.readObject();
		}
		return publicKey;
	}
	
	/**
	 * Loads the PrivateKey from private.key
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey() throws Exception {
		if(privateKey == null) {
			ObjectInputStream ois = new ObjectInputStream(RSAUtil.class.getClassLoader().getResourceAsStream("rsa/private.key"));
			return (PrivateKey)ois.readObject();
		}
		return privateKey;
	}
	
	/**
	 * Encrypts the string and returns to result as Hex String.
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static String encryptToHex(String string) throws Exception {
		byte[] enc = encrypt(string);
		return byteArrayToHexString(enc);
	}
	
	/**
	 * Encrypts the string.
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String string) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		byte[] data = cipher.doFinal(string.getBytes());
		return data;
	}
	
	/**
	 * Decrypt the Hex String.
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static String decryptFromHexString(String enc) throws Exception {
		byte[] data = hexStringToByteArray(enc);
		return decrypt(data);
	}
	
	/**
	 * Decrypts the byte array data.
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		byte[] decData = cipher.doFinal(data);
		return new String(decData);
	}
	
	/**
	 * Converts a byte[] to Hex String. Used to convert byte array of encrypted data to string.
	 * @param data
	 * @return
	 */
	public static String byteArrayToHexString(byte[] data) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (data[i] & 0x00FF)).substring(1));
        }
        return retString.toString();
	}
	
	/**
	 * Converts hex string to byte[]. Used to convert string encrypted data to byte[] for decryption.
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
