package com.exalt.ipc.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.exalt.ipc.entities.User;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.exalt.ipc.utilities.Constants.*;

@Service
public class HelperSerivce {

	public static String buildToken(User user) {
		return JWT.create().withSubject(user.getEmail())
							.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).withClaim("role", user.getRole())
							.sign(HMAC512(SECRET.getBytes()));
	}

	public static DecodedJWT getDecodedJwt(String jwt) {
		return JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(jwt.replace(TOKEN_PREFIX, ""));
	}

	public String getEmail(String jwt) {
		//		System.out.println("getEmail() subject email " + getDecodedJwt(jwt).getSubject());
		return getDecodedJwt(jwt).getSubject();
	}

}
