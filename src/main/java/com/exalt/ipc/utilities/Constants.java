package com.exalt.ipc.utilities;

public class Constants {
	public static final String SECRET = "SecretKeyToGenJWTs";

	public static final long EXPIRATION_TIME = 864_000_000; // 10 days

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String AUTHORIZATION = "Authorization";

	public static final String[] SIGN_UP_URL = new String[]{"/v1/users/signup"};

	public static final String[] PUBLIC_END_POINTS =
			new String[]{"/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/v1/users/login",
					"/v1/users/signup"};

	public static final int IPC_QUEUE_LIMIT = 20;

	public static final long HELD_QUEUE_SIZE_LIMIT = 150_000_000;

	public static final int Printing_QUEUE_LIMIT = 10;

	public static final String JWT_TOKEN = "Authorization Bearer jwt token";

	public static final int CREATED = 201;

	public static final int NOT_FOUND = 404;

	public static final int OK = 200;

	public static final int BAD_REQUEST = 400;

	public static final int UNSUPPORTED_MEDIA_TYPE = 415;

}
