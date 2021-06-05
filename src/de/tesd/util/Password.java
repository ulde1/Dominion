package de.tesd.util;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;


/** Password
 * Routinen zur Kennwort-Verwaltung
 *
 * @author Roland M. Eppelt
 */
public class Password {
	
//	private final static Logger LOG = Logger.getLogger(Password.class.getName());
	
	
	public static <T> T[] concat(T[] a, T[] b) {
		T[] result = Arrays.copyOf(a, a.length+b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	
	public static String hex(byte[] a) {
		return String.format("%0"+2*a.length+"x", new BigInteger(1, a));
	}
	
	
	public static byte[] unhex(String hex) {
		return new BigInteger(hex, 16).toByteArray();
	}
	
	
	public static byte[] concat(byte[] a, byte[] b) {
		byte[] result = Arrays.copyOf(a, a.length+b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	
	public static byte[] hash(byte[] message, MessageDigest digest, int count) {
		byte[] result = message;
		for (int i = 0; i<count; i++)
			result = digest.digest(result);
		return result;
	}
	
	
	final static public int HASH_COUNT = 973;
	
	
	public static byte[] hash(byte[] message, MessageDigest digest) {
		return hash(message, digest, HASH_COUNT);
	}
	
	
	public static byte[] hash(byte[] message) throws NoSuchAlgorithmException {
//		try {
			return hash(message, MessageDigest.getInstance("SHA-256"));
//		} catch (NoSuchAlgorithmException ex) {
//			log.warning(ex.getMessage());
//			try {
//				return hash(message, MessageDigest.getInstance("SHA-1"));
//			} catch (NoSuchAlgorithmException ex1) {
//				log.warning(ex.getMessage());
//				try {
//					return hash(message, MessageDigest.getInstance("MD5")); // gilt als geknackt
//				} catch (NoSuchAlgorithmException ex2) {
//					log.severe(ex.getMessage());
//					return null;
//				}
//			}
//		}
	}
	
		
	final static private int SALT_LENGTH = 8;
	
	public static byte[] calcPasswordSalt(String eMail) throws NoSuchAlgorithmException {
		return Arrays.copyOfRange(hash(eMail.getBytes()), 3, 3+SALT_LENGTH);
	}
	
	
	public static boolean checkSalt(String eMail, String sHash) throws NoSuchAlgorithmException {
		boolean result = true;
		byte[] bSalt = calcPasswordSalt(eMail);
		byte[] bHash = Base64.getDecoder().decode(sHash);
		for (int i = 0; i<SALT_LENGTH; i++)
			result &= bHash[i]==bSalt[i];
		return result;
	}
	
	
	static private Random rnd = new Random();
	public static final String CHARS_UD = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String CHARS_ULD="123456789abcdefgihjkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

	
	
	static public String randomString(String RandChars, int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i<len; i++) 
			sb.append(RandChars.charAt(rnd.nextInt(RandChars.length())));
		return sb.toString();
	}

	
	/** Erzeugt ein zufälliges Kennwort aus Ziffern, großen und kleinen Buchstaben */
	public static String generateNewPassword(int length) {
		return randomString(CHARS_ULD, length);
	}
	
	
	public static byte[] hashPassword(byte[] salt, String password) throws NoSuchAlgorithmException {
		return concat(salt, hash(concat(salt, password.getBytes())));
	}

	
}
