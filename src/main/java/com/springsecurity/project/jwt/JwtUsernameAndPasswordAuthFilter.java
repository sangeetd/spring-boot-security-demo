package com.springsecurity.project.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter{

	@Value("${project.jwt.key:'secureKeyHere'}")
	private String key="secureKeyHere";
	
	private final AuthenticationManager authenticationManager;
	
	public JwtUsernameAndPasswordAuthFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		UsernamePasswordAuthModel usernamePasswordAuthModel;
		try {
			usernamePasswordAuthModel=new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordAuthModel.class);
			Authentication authentication=new UsernamePasswordAuthenticationToken(
					usernamePasswordAuthModel.getUsername(), 
					usernamePasswordAuthModel.getPassword());
			return authenticationManager.authenticate(authentication);
		
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		System.out.println("key:"+JwtKeyEnum.KEY.toString());
		
		String token=Jwts.builder()
		.setSubject(authResult.getName())
		.claim("authorities", authResult.getAuthorities())
		.setIssuedAt(new Date())
		.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(3)))
		.signWith(SignatureAlgorithm.HS512, JwtKeyEnum.KEY.toString().getBytes())
		.compact();
		
		response.addHeader("Authorization", "Bearer "+token);
		
	}

	
	
}
