package vn.inspiron.mcontract.modules.Authentication.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.dto.JwtTokenRequestDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.JwtTokenResponseDTO;
import vn.inspiron.mcontract.modules.Authentication.model.UserAuth;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Slf4j
public class JwtAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EncryptorAesGcmService encryptorAesGcmService;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt-secret-key}")
    private String secretKey;

    public UserEntity authenticate(JwtTokenRequestDTO tokenRequest) throws AuthenticationException {
        UserAuth userAuth = managerAuthentication(tokenRequest.getUsername(), tokenRequest.getPassword());
        return userAuth.getUserEntity();
    }

    public JwtTokenResponseDTO generateAccessToken(
            String subject,
            String url,
            TimeZone timeZone,
            int timeLiveInMinute
    ) throws Exception {
        subject = this.encryptorAesGcmService.encrypt(subject.getBytes());
        String token = this.generateToken(subject, url, timeZone, timeLiveInMinute);
        return new JwtTokenResponseDTO(token);
    }

    public JwtTokenResponseDTO generateRefreshToken(
            String subject,
            String password,
            String code,
            String url,
            TimeZone timeZone,
            int timeLiveInMinute
    ) throws Exception {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

        subject = this.encryptorAesGcmService.encrypt(subject.getBytes());
        password = this.encryptorAesGcmService.encrypt(password.getBytes());
        code = this.encryptorAesGcmService.encrypt(code.getBytes());

        Algorithm algorithm = Algorithm.HMAC512(this.secretKey);
        String token = JWT.create()
                .withIssuer(url)
                .withSubject(subject)
                .withClaim("code1", password)
                .withClaim("code2", code)
                .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(timeLiveInMinute).toInstant()))
                .sign(algorithm);

        return new JwtTokenResponseDTO(token);
    }

    public JwtTokenResponseDTO refreshAccessToken(
            String code,
            Cookie cookie,
            String url,
            TimeZone timeZone,
            int timeLiveInMinute
    ) throws Exception {
        Algorithm algorithm = Algorithm.HMAC512(this.secretKey);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(cookie.getValue());

        Long userID = Long.parseLong(this.encryptorAesGcmService.decrypt(decodedJWT.getSubject()));
        Optional<UserEntity> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            throw new Exception("Cannot create access token");
        }

        String cCode = this.encryptorAesGcmService.decrypt(decodedJWT.getClaim("code2").asString());
        String cPassword = this.encryptorAesGcmService.decrypt(decodedJWT.getClaim("code1").asString());

        if (!(code.equals(cCode) && user.get().getPassword().equals(cPassword))) {
            throw new Exception("Cannot create access token");
        }

        return new JwtTokenResponseDTO(this.generateToken(decodedJWT.getSubject(), url, timeZone, timeLiveInMinute));
    }

    private String generateToken(String userToken, String url, TimeZone timeZone, int timeLiveInMinute) throws Exception {
        userToken = this.encryptorAesGcmService.encrypt(userToken.getBytes());
        Instant now = Instant.now();
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

        Algorithm algorithm = Algorithm.HMAC512(this.secretKey);

        return JWT.create()
                .withIssuer(url)
                .withSubject(userToken)
                .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(timeLiveInMinute).toInstant()))
                .sign(algorithm);
    }

    private UserAuth managerAuthentication(String username, String password) throws AuthenticationException
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return (UserAuth) authenticate.getPrincipal();
    }

    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
