package com.cb.common.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 生成工具类
 */
public class JwtUtil {

	/**
	 * 签发 jwt
	 *
	 * @param id        令牌id
	 * @param subject   主题
	 * @param ttlMillis jwt有效期
	 * @return jwt
	 */
	public static String createJwt(String id, String subject, Long ttlMillis, String name, Object data) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		if (ttlMillis == null) {
			// 如果没有设置时间，设置默认过期时间：1天
			ttlMillis = JwtProperties.expiration;
		}
		long expMillis = nowMillis + ttlMillis;
		Date expDate = new Date(expMillis);
		SecretKey secretKey = generalKey();
		// 载荷内容为null不再添加额外载荷
		JwtBuilder builder = null;
		if (name == null || data == null) {
			builder = Jwts.builder()
					.setId(id)
					// 主题  可以是JSON数据
					.setSubject(subject)
					// 签发者
					.setIssuer(JwtProperties.issuer)
					// 签发时间
					.setIssuedAt(now)
					//使用HS256对称加密算法签名, 第二个参数为秘钥
					.signWith(signatureAlgorithm, secretKey)
					// 设置过期时间
					.setExpiration(expDate);
		} else {
			builder = Jwts.builder()
					.setId(id)
					// 主题  可以是JSON数据
					.setSubject(subject)
					// 签发者
					.setIssuer(JwtProperties.issuer)
					// 签发时间
					.setIssuedAt(now)
					// 设置载荷信息
					.claim(name, data)
					//使用HS256对称加密算法签名, 第二个参数为秘钥
					.signWith(signatureAlgorithm, secretKey)
					// 设置过期时间
					.setExpiration(expDate);
		}

		return builder.compact();
	}

	/**
	 * 添加指定载荷签发jwt
	 *
	 * @return jwt
	 */
	public static String createJwt(String name, Object data) {
		return createJwt(UUID.randomUUID().toString(), "chatbucket", null, name, data);
	}

	/**
	 * 无参使用默认配置签发jwt
	 *
	 * @return jwt
	 */
	public static String createJwt() {
		return createJwt(UUID.randomUUID().toString(), "chatbucket", null, null, null);
	}

	/**
	 * 生成加密后的秘钥 secretKey
	 *
	 * @return 秘钥
	 */
	public static SecretKey generalKey() {
		byte[] encodedKey = Base64.getDecoder().decode(JwtProperties.key);
		return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	}

	/**
	 * 解析 jwt，如果解析错误，将抛出异常
	 *
	 * @param jwt token
	 * @return 解析值
	 * @throws Exception 解析异常
	 */
	public static Claims parseJwt(String jwt) throws Exception {
		SecretKey secretKey = generalKey();
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(jwt)
				.getBody();
	}

}