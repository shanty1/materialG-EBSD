/**
 * DES,AES加密
 */
package per.sc.tool.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类. 支持Hex与Base64两种编码方式.
 * 
 * @author calvin
 */
public class CryptoUtils {

	private static final String DES = "DES";
	private static final String AES = "AES";
	private static final String HMACSHA1 = "HmacSHA1";

	private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;// RFC2401
	private static final int DEFAULT_AES_KEYSIZE = 128;
	private static final String CHATSET = "UTF-8";

	// -- HMAC-SHA1 funciton --//
	/**
	 * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes HMAC-SHA1密钥
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 */
	public static byte[] hmacSha1(String input, byte[] keyBytes) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyBytes, HMACSHA1);
			Mac mac = Mac.getInstance(HMACSHA1);
			mac.init(secretKey);
			return mac.doFinal(input.getBytes(CHATSET));
		} catch (Exception e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Hex编码的结果,长度为40字符.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @see #hmacSha1(String, byte[])
	 */
	public static String hmacSha1ToHex(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.hexEncode(macResult);
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Base64编码的结果.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @see #hmacSha1(String, byte[])
	 */
	public static String hmacSha1ToBase64(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.base64Encode(macResult);
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Base64编码的URL安全的结果.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @see #hmacSha1(String, byte[])
	 */
	public static String hmacSha1ToBase64UrlSafe(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.base64UrlSafeEncode(macResult);
	}

	/**
	 * 校验Hex编码的HMAC-SHA1签名是否正确.
	 * 
	 * @param hexMac Hex编码的签名
	 * @param input 原始输入字符串
	 * @param keyBytes 密钥
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 */
	public static boolean isHexMacValid(String hexMac, String input, byte[] keyBytes) {
		byte[] expected = EncodeUtils.hexDecode(hexMac);
		byte[] actual = hmacSha1(input, keyBytes);

		return Arrays.equals(expected, actual);
	}

	/**
	 * 校验Base64/Base64URLSafe编码的HMAC-SHA1签名是否正确.
	 * 
	 * @param base64Mac Base64/Base64URLSafe编码的签名
	 * @param input 原始输入字符串
	 * @param keyBytes 密钥
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 */
	public static boolean isBase64MacValid(String base64Mac, String input, byte[] keyBytes) {
		byte[] expected = EncodeUtils.base64Decode(base64Mac);
		byte[] actual = hmacSha1(input, keyBytes);
		return Arrays.equals(expected, actual);
	}

	/**
	 * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
	 */
	public static byte[] generateMacSha1Key() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
			keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 生成HMAC-SHA1密钥, 返回Hex编码的结果,长度为40字符.
	 * 
	 * @see #generateMacSha1Key()
	 */
	public static String generateMacSha1HexKey() {
		return EncodeUtils.hexEncode(generateMacSha1Key());
	}

	// -- DES function --//
	/**
	 * 使用DES加密原始字符串, 返回Hex编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desEncryptToHex(String input, byte[] keyBytes) {
		try {
			byte[] encryptResult = des(input.getBytes(CHATSET), keyBytes, Cipher.ENCRYPT_MODE);
			return EncodeUtils.hexEncode(encryptResult);
		} catch (Exception e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 使用DES加密原始字符串, 返回Hex编码的结果.
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String desEncryptToHex(String input, String key) {
		try {
			return desEncryptToHex(input, key.getBytes(CHATSET));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 使用DES加密原始字符串, 返回Base64编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合DES要求的密钥
	 * @throws UnsupportedEncodingException
	 */
	public static String desEncryptToBase64(String input, byte[] keyBytes) {
		byte[] encryptResult;
		try {
			encryptResult = des(input.getBytes(CHATSET), keyBytes, Cipher.ENCRYPT_MODE);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
		return EncodeUtils.base64Encode(encryptResult);
	}

	/**
	 * 使用DES解密Hex编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desDecryptFromHex(String input, byte[] keyBytes) {
		try {
			byte[] decryptResult = des(EncodeUtils.hexDecode(input), keyBytes, Cipher.DECRYPT_MODE);
			return new String(decryptResult, "UTF-8");
		} catch (Exception e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 使用DES解密Hex编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String desDecryptFromHex(String input, String key) {
		try {
			return desDecryptFromHex(input, key.getBytes(CHATSET));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 使用DES解密Base64编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Base64编码的加密字符串
	 * @param keyBytes 符合DES要求的密钥
	 * @throws UnsupportedEncodingException
	 */
	public static String desDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = des(EncodeUtils.base64Decode(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用DES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param inputBytes 原始字节数组
	 * @param keyBytes 符合DES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] des(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(inputBytes);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 生成符合DES要求的密钥, 长度为64位(8字节).
	 */
	public static byte[] generateDesKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 生成符合DES要求的Hex编码密钥, 长度为16字符.
	 */
	public static String generateDesHexKey() {
		return EncodeUtils.hexEncode(generateDesKey());
	}

	// -- AES funciton --//
	/**
	 * 使用AES加密原始字符串, 返回Hex编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合AES要求的密钥
	 * @throws UnsupportedEncodingException
	 */
	public static String aesEncryptToHex(String input, byte[] keyBytes) {
		byte[] encryptResult;
		try {
			encryptResult = aes(input.getBytes(CHATSET), keyBytes, Cipher.ENCRYPT_MODE);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
		return EncodeUtils.hexEncode(encryptResult);
	}

	/**
	 * 使用AES加密原始字符串, 返回Base64编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合AES要求的密钥
	 * @throws UnsupportedEncodingException
	 */
	public static String aesEncryptToBase64(String input, byte[] keyBytes) {
		byte[] encryptResult;
		try {
			encryptResult = aes(input.getBytes(CHATSET), keyBytes, Cipher.ENCRYPT_MODE);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
		return EncodeUtils.base64Encode(encryptResult);
	}

	/**
	 * 使用AES解密Hex编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesDecryptFromHex(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.hexDecode(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用AES解密Base64编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Base64编码的加密字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.base64Decode(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param inputBytes 原始字节数组
	 * @param keyBytes 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(inputBytes);
		} catch (Exception e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 生成AES密钥,返回字节数组,长度为128位(16字节).
	 */
	public static byte[] generateAesKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(DEFAULT_AES_KEYSIZE);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 生成AES密钥, 返回Hex编码的结果,长度为32字符.
	 * 
	 * @see #generateMacSha1Key()
	 */
	public static String generateAesHexKey() {
		return EncodeUtils.hexEncode(generateAesKey());
	}

	public static void main(String[] args) {
		// String token = "4faa8662c59590c6f43ae9fe5b002b42";
		String key = "Z(AfY@XS";

		// 解密
		String sn = "a5c2646a23511d9f057b5fc37f1652069a9dd79478c77b2f20995c2e3e5dbe8993ea41b31eaf4b2b592ef9207c3c945b429f289a520dc5d1c2aa416e2aef041ca15f42882b31f71ba3dfc5d759c811a93b059126cab37881204d1c4face8efd2075210690c40ddf0afe47e37a4ea5cb7e117fa07b9d05d31d7db20fda892f3e711ee6d38f79551cc";
		System.out.println(CryptoUtils.desDecryptFromHex(sn, key));

		// 加密
		JSONObject params = new JSONObject();
		params.put("_time", System.currentTimeMillis());
		params.put("circleId", 9229);
		params.put("uid", 42);
		params.put("maxW", 500);
		System.out.println(CryptoUtils.desEncryptToHex(params.toJSONString(), key));
	}

}