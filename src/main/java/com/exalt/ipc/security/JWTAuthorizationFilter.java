package com.exalt.ipc.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.exalt.ipc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.exalt.ipc.utilities.Constants.*;

/*
 * BasicAuthenticationFilter Processes a HTTP request's BASIC authorization
 *  headers, putting the result into the
 * <code>SecurityContextHolder</code>.
 * */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	UserService userService;

	@Autowired
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
		super(authenticationManager);
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			DecodedJWT decodedJWT =
					JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, ""));
			String email = decodedJWT.getSubject();
			List list = new ArrayList<>();
			String role = decodedJWT.getClaim("role").asString();
			if (email != null && role != null && !isExpired(decodedJWT.getExpiresAt())) {
				if (userService.getUserOptional(email).isPresent()) {
					list.add(new SimpleGrantedAuthority(role));
					return new UsernamePasswordAuthenticationToken(email, null, list);
				} else {
					return null;
				}
			}
			return null;
		}
		return null;
	}

	public boolean isExpired(Date date) {
		return new Date().after(date);
	}
}
