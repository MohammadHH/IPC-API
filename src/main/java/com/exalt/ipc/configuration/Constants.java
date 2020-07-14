package com.exalt.ipc.configuration;

public class Constants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String[] SIGN_UP_URL = new String[]{"/v1/users/signup"};
    public static final String[] PUBLIC_END_POINTS = new String[]{"/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/v1/users/login", "/v1/users/signup"};
    public static final int IPC_QUEUE_LIMIT = 20;
    public static final long HELD_QUEUE_SIZE_LIMIT = 150_000_000;
    public static final int Printing_QUEUE_LIMIT = 10;


}
