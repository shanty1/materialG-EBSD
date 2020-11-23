/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: EncodeUtils.java,v 1.1 2012/05/30 12:04:24 xinsheng Exp $
 */
package com.kglab.tool.util.crypt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * 各种格式的编码加码工具类.
 * 
 * 集成Commons-Codec,Commons-Lang及JDK提供的编解码方法.
 * 
 * @author calvin
 */
public class EncodeUtils extends StringEscapeUtils {
	
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	
	private static String Algorithm = "DES"; // 定义 加密算法,可用 DES,DESede,Blowfish
	
	public static boolean debug = false;
	
	static {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}
	/**
	 * Hex编码.
	 */
	public static String hexEncode(byte[] input) {
		return Hex.encodeHexString(input).toUpperCase();
	}
	
	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}
	
	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
	
	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}
	
	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}
	
	 /**
	  * 将 s 进行 BASE64 编码(如果失败则返回原字符串)
	  * @author shuchao
	  * @data   2019年3月5日
	  * @param s
	  * @return 
	  */
    public static String base64Encode2(String s) {
        if (s == null)
            return null;
        try {
           return Base64.encodeBase64String(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }

	/**
	 * 将 BASE64 编码的字符串 s 进行解码(如果失败则返回原字符串)
	 * @author shuchao
	 * @data   2019年3月5日
	 * @param s
	 * @return
	 */
    public static String base64Decode2(String s) {
        if (s == null)
            return null;
        try {
            return new String(Base64.decodeBase64(s),"UTF-8");
        } catch (Exception e) {
        	e.printStackTrace();
            return s;
        }
    }
	
	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String input) {
		return urlEncode(input, DEFAULT_URL_ENCODING);
	}
	
	/**
	 * URL 编码.
	 */
	public static String urlEncode(String input, String encoding) {
		try {
			return URLEncoder.encode(input, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}
	
	/**
	 * 只将中文进行URLEncoder编码为utf-8
	 * 
	 * @param input
	 * @return
	 */
	public static String urlEncodeZh(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			String s = input.substring(i, i + 1);
			if (s.matches("[\\u4E00-\\u9FA5]+")) {
				sb.append(EncodeUtils.urlEncode(s));
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 只将中文进行URLEncoder编码为encoding
	 * 
	 * @param input
	 * @param encoding
	 * @return
	 */
	public static String urlEncodeZh(String input, String encoding) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			String s = input.substring(i, i + 1);
			if (s.matches("[\\u4E00-\\u9FA5]+")) {
				sb.append(EncodeUtils.urlEncode(s, encoding));
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String input) {
		return urlDecode(input, DEFAULT_URL_ENCODING);
	}
	
	/**
	 * URL 解码.
	 */
	public static String urlDecode(String input, String encoding) {
		try {
			return URLDecoder.decode(input, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}
	
 
	/**
	 * 此方法与javascript中的escape实现一致
	 * 
	 * @param src
	 * @return
	 */
	public static String escapeJS(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
	
	/**
	 * 此方法与javascript中的unescape实现一致
	 * 
	 * @param src
	 * @return
	 */
	public static String unescapeJS(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	
	
	
	
	// 生成密钥, 注意此步骤时间比较长
	public static byte[] getKey() throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
		if (debug)
			System.out.println("生成密钥:" + byte2hex(deskey.getEncoded()));
		return deskey.getEncoded();
	}
	
	/**
	 * des加密
	 * <br><br>
	 * 加密模式：DES-ECB，
	 * 填充模式：pkcs5padding | pkcs7padding，
	 * 输出：16进制字符串
	 * @param input 需加密原文本
	 * @param key 密钥
	 * @return 加密后的16进制字符串
	 * @throws Exception
	 * @date 2018年5月30日
	 * @author 舒超
	 */
	public static String desEncode(String input, String key) throws Exception {
		return byte2hex(desEncode(input.getBytes(), key.getBytes()));
	}
	
	/**
	 * des加密
	 * <br><br>
	 * 加密模式：DES-ECB，
	 * 填充模式：pkcs5padding | pkcs7padding，
	 * 输出：16进制字符串
	 * @param input 需加密原文本字节数组
	 * @param key 密钥字节数组
	 * @return 加密后的16进制字符串
	 * @throws Exception
	 * @date 2018年5月30日
	 * @author 舒超
	 */
	public static byte[] desEncode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug) {
			System.out.println("加密前的二进串:" + byte2hex(input));
			System.out.println("加密前的字符串:" + new String(input));
		}
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		if (debug)
			System.out.println("加密后的二进串:" + byte2hex(cipherByte));
		return cipherByte;
	}
	
	/**
	 * des解密
	 * <br><br>
	 * 加密模式：DES-ECB，
	 * 填充模式：pkcs5padding | pkcs7padding，
	 * 输出：16进制字符串
	 * @param input 已加密文本
	 * @param key 密钥
	 * @return 加密后的16进制字符串
	 * @throws Exception
	 * @date 2018年5月30日
	 * @author 舒超
	 */
	public static String desDecode(String input, String key) throws Exception {
		return new String(desDecode(hex2byte(input), key.getBytes()));
	}
	/**
	 * des解密（指定解密编码）
	 * <br><br>
	 * 加密模式：DES-ECB，
	 * 填充模式：pkcs5padding | pkcs7padding，
	 * 输出：16进制字符串
	 * @param input 已加密文本
	 * @param key 密钥
	 * @param charset 解密编码
	 * @return 加密后的16进制字符串
	 * @throws Exception
	 * @date 2018年5月30日
	 * @author 舒超
	 */
	public static String desDecode(String input, String key, String charset) throws Exception {
		return new String(desDecode(hex2byte(input), key.getBytes()), charset);
	}
	/**
	 * des解密密
	 * <br><br>
	 * 加密模式：DES-ECB，
	 * 填充模式：pkcs5padding | pkcs7padding，
	 * 输出：16进制字符串
	 * @param input 已加密文本字节数组
	 * @param key 密钥字节数组
	 * @return 加密后的16进制字符串
	 * @throws Exception
	 * @date 2018年5月30日
	 * @author 舒超
	 */
	public static byte[] desDecode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug)
			System.out.println("解密前的信息:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		if (debug) {
			System.out.println("解密后的二进串:" + byte2hex(clearByte));
			System.out.println("解密后的字符串:" + (new String(clearByte)));
		}
		return clearByte;
	}

	public static String md5to16(String encryptStr) throws NoSuchAlgorithmException {
		return md5(encryptStr).substring(8, 24);
	}

	
	public static String md5(String input) throws NoSuchAlgorithmException {
		return byte2hex(md5(input.getBytes()));
	}
	
	// md5()信息摘要, 不可逆
	public static byte[] md5(byte[] input) throws NoSuchAlgorithmException     {
		java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5"); // or "SHA-1"
		if (debug) {
			System.out.println("摘要前的二进串:" + byte2hex(input));
			System.out.println("摘要前的字符串:" + new String(input));
		}
		alg.update(input);
		byte[] digest = alg.digest();
		if (debug)
			System.out.println("摘要后的二进串:" + byte2hex(digest));
		return digest;
	}
	
	// 字节码转换成16进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n < b.length - 1)
			// hs = hs + ":";
		}
		// System.out.println("hs="+hs);
		return hs.toUpperCase();
	}
	
	// 将16进制字符串转换成字节码
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(md5("234"));
	}
	/*
	 * 加密算法(DES,AES,RSA,MD5,SHA1,Base64)比较和项目应用

	加密技术通常分为两大类:"对称式"和"非对称式"。

	对称性加密算法：对称式加密就是加密和解密使用同一个密钥。信息接收双方都需事先知道密匙和加解密算法且其密匙是相同的，之后便是对数据进行加解密了。对称加密算法用来对敏感数据等信息进行加密。

	非对称算法：非对称式加密就是加密和解密所使用的不是同一个密钥，通常有两个密钥，称为"公钥"和"私钥"，它们两个必需配对使用，否则不能打开加密文件。发送双方A,B事先均生成一堆密匙，然后A将自己的公有密匙发送给B，B将自己的公有密匙发送给A，如果A要给B发送消 息，则先需要用B的公有密匙进行消息加密，然后发送给B端，此时B端再用自己的私有密匙进行消息解密，B向A发送消息时为同样的道理。

	散列算法：散列算法，又称哈希函数，是一种单向加密算法。在信息安全技术中，经常需要验证消息的完整性，散列(Hash)函数提供了这一服务，它对不同长度的输入消息，产生固定长度的输出。这个固定长度的输出称为原输入消息的"散列"或"消息摘要"(Message digest)。散列算法不算加密算法，因为其结果是不可逆的，既然是不可逆的，那么当然不是用来加密的，而是签名。
	 

	对称性加密算法有：AES、DES、3DES
	用途：对称加密算法用来对敏感数据等信息进行加密

	DES（Data Encryption Standard）：数据加密标准，速度较快，适用于加密大量数据的场合。

	3DES（Triple DES）：是基于DES，对一块数据用三个不同的密钥进行三次加密，强度更高。

	AES（Advanced Encryption Standard）：高级加密标准，是下一代的加密算法标准，速度快，安全级别高；AES是一个使用128为分组块的分组加密算法，分组块和128、192或256位的密钥一起作为输入，对4×4的字节数组上进行操作。众所周之AES是种十分高效的算法，尤其在8位架构中，这源于它面向字节的设计。AES 适用于8位的小型单片机或者普通的32位微处理器,并且适合用专门的硬件实现，硬件实现能够使其吞吐量（每秒可以到达的加密/解密bit数）达到十亿量级。同样，其也适用于RFID系统。


	非对称性算法有：RSA、DSA、ECC

	RSA：由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的。RSA在国外早已进入实用阶段，已研制出多种高速的RSA的专用芯片。

	DSA（Digital Signature Algorithm）：数字签名算法，是一种标准的 DSS（数字签名标准），严格来说不算加密算法。

	ECC（Elliptic Curves Cryptography）：椭圆曲线密码编码学。ECC和RSA相比，具有多方面的绝对优势，主要有：抗攻击性强。相同的密钥长度，其抗攻击性要强很多倍。计算量小，处理速度快。ECC总的速度比RSA、DSA要快得多。存储空间占用小。ECC的密钥尺寸和系统参数与RSA、DSA相比要小得多，意味着它所占的存贮空间要小得多。这对于加密算法在IC卡上的应用具有特别重要的意义。带宽要求低。当对长消息进行加解密时，三类密码系统有相同的带宽要求，但应用于短消息时ECC带宽要求却低得多。带宽要求低使ECC在无线网络领域具有广泛的应用前景。


	散列算法（签名算法）有：MD5、SHA1、HMAC
	用途：主要用于验证，防止信息被修。具体用途如：文件校验、数字签名、鉴权协议

	MD5：MD5是一种不可逆的加密算法，目前是最牢靠的加密算法之一，尚没有能够逆运算的程序被开发出来，它对应任何字符串都可以加密成一段唯一的固定长度的代码。

	SHA1：是由NISTNSA设计为同DSA一起使用的，它对长度小于264的输入，产生长度为160bit的散列值，因此抗穷举(brute-force)性更好。SHA-1设计时基于和MD4相同原理,并且模仿了该算法。SHA-1是由美国标准技术局（NIST）颁布的国家标准，是一种应用最为广泛的Hash函数算法，也是目前最先进的加密技术，被政府部门和私营业主用来处理敏感的信息。而SHA-1基于MD5，MD5又基于MD4。

	HMAC：是密钥相关的哈希运算消息认证码（Hash-based Message Authentication Code）,HMAC运算利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。也就是说HMAC是需要一个密钥的。所以，HMAC_SHA1也是需要一个密钥的，而SHA1不需要。

	其他常用算法：

	Base64：其实不是安全领域下的加密解密算法，只能算是一个编码算法，通常用于把二进制数据编码为可写的字符形式的数据，对数据内容进行编码来适合传输(可以对img图像编码用于传输)。这是一种可逆的编码方式。*/

}
