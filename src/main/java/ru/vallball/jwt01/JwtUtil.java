package ru.vallball.jwt01;

import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

	@Value("$(jwt.secret)")
	private String jwtSecret;

	public String generateToken(String login) {
		Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return Jwts.builder().setSubject(login).setExpiration(date).signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException expEx) {
			System.out.println("Token expired");
		} catch (UnsupportedJwtException unsEx) {
			System.out.println("Unsupported jwt");
		} catch (MalformedJwtException mjEx) {
			System.out.println("Malformed jwt");
		} catch (SignatureException sEx) {
			System.out.println("Invalid signature");
		} catch (Exception e) {
			System.out.println("invalid token");
		}
		return false;
	}

	public String getLoginFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
