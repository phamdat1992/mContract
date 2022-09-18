package vn.amitgroup.digitalsignatureapi.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import vn.amitgroup.digitalsignatureapi.dto.SignerInfo;
import vn.amitgroup.digitalsignatureapi.entity.TokenIsDestroy;
import vn.amitgroup.digitalsignatureapi.security.MyUserDetails;
import vn.amitgroup.digitalsignatureapi.service.TokenIsDestroyService;

@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.expiration}")
	private int jwtExpiration;
	@Value("${s3.Properties.expiredMilliSecondTime}")
    private Long expiredMilliSecondTime;
	@Autowired
	private TokenIsDestroyService tokenIsDestroyService;

	public String generateToken(Authentication authentication) {
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
			claims.put("isManager", true);
		}

		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+jwtExpiration * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}
	public String generateForSigner(String email,String contractId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("contractId", contractId);
		return Jwts.builder()
				.setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
	public String generateForDownload(Long fileId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("fileId", fileId);
		return Jwts.builder()
				.setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiredMilliSecondTime * 86400000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

	public String doGenerateRefreshToken(String subject) {

		return Jwts.builder().setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpiration * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}

	public boolean validateToken(String authToken) throws BadCredentialsException{
		try {
			List<TokenIsDestroy> destroys = tokenIsDestroyService.get(authToken);
			if (destroys.size() > 0) {
				throw new BadCredentialsException("INVALID_CREDENTIALS");
			}
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}

	public String getUserAccount(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	public SignerInfo getSignerInfo(String token) {
        String email= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		String contractId = claims.get("contractId", String.class);
		SignerInfo signerInfo= new SignerInfo();
		signerInfo.setSignerEmail(email);
		signerInfo.setContracId(contractId);
		return signerInfo;
	}
	public long getFileId(String token){
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		Long fileId = claims.get("fileId", Long.class);
		return fileId;
	}
	public Date getIssuedAt(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return claims.getExpiration();
	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		List<SimpleGrantedAuthority> roles = null;

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isUser = claims.get("isManager", Boolean.class);

		if (isAdmin != null && isAdmin) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		if (isUser != null && isAdmin) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"));
		}
		return roles;

	}

}
