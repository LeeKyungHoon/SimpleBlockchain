package security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class StringUtil {

	public static String applySha256(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			
			for(int i=0;i<hash.length;i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() ==1 )hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return output;
	}
	
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static PublicKey getKeyFromString(String key) {

	 byte[] publicBytes = Base64.getDecoder().decode((key.getBytes()));
	 X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
	 KeyFactory keyFactory;
	 PublicKey pubKey = null;
	try {
		keyFactory = KeyFactory.getInstance("ECDSA", "BC");
		pubKey = keyFactory.generatePublic(keySpec);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 
	 return pubKey;
	 
	}	
}
