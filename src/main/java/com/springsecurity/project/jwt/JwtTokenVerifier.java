package com.springsecurity.project.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter{

	@Value("${project.jwt.key:'secureKeyHere'}")
	private String key="secureKeyHere";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader=request.getHeader("Authorization");
		if(authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			
			
			System.out.println("----key:"+JwtKeyEnum.KEY.toString());
			
			String token=authorizationHeader.replace("Bearer ", "");
			
			Jws<Claims> claims = Jwts.parser()
			.setSigningKey(JwtKeyEnum.KEY.toString().getBytes())
			.parseClaimsJws(token);
			
			Claims body=claims.getBody();
			String username=body.getSubject();
			List<Map<String, String>> authorities=(List<Map<String, String>>)body.get("authorites");
			
			Set<SimpleGrantedAuthority> grantedAuthorities=authorities.stream()
			.map(m -> new SimpleGrantedAuthority(m.get("auhtority")))
			.collect(Collectors.toSet());
			
			Authentication authentication=new UsernamePasswordAuthenticationToken(
					username, 
					null,
					grantedAuthorities);
					
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}catch(Exception e) {
			throw new IllegalStateException("Token unable to trust "+e.getMessage());
		}
		
		
	}

}
