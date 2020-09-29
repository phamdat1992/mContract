package vn.inspiron.mcontract.modules.Authentication.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.Cookie;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@Service
@Slf4j
public class JwtAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final String SECRET_KEY = "key";

    public JwtTokenResponseDTO authenticate(JwtTokenRequestDTO tokenRequest, String url, TimeZone timeZone, int timeLiveInMinute) throws AuthenticationException {
        UserAuth userAuth = managerAuthentication(tokenRequest.getUsername(), tokenRequest.getPassword());
        String token = generateToken(userAuth.getUserEntity().getToken(), url, timeZone, timeLiveInMinute);

        return new JwtTokenResponseDTO(token);
    }

    public JwtTokenResponseDTO generateRefreshToken(String subject, String url, TimeZone timeZone, int timeLiveInMinute) {
        try {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = JWT.create()
                    .withIssuer(url)
                    .withSubject(subject)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(timeLiveInMinute).toInstant()))
                    .sign(algorithm);

            return new JwtTokenResponseDTO(token);
        }
        catch (JWTCreationException e)
        {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    public JwtTokenResponseDTO refreshAccessToken(Cookie cookie, String url, TimeZone timeZone, int timeLiveInMinute) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(cookie.getValue());

        return new JwtTokenResponseDTO(generateToken(decodedJWT.getSubject(), url, timeZone, timeLiveInMinute));
    }

    private String generateToken(String userToken, String url, TimeZone timeZone, int timeLiveInMinute)
    {
        try
        {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = JWT.create()
                    .withIssuer(url)
                    .withSubject(userToken)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(timeLiveInMinute).toInstant()))
                    .sign(algorithm);

            return token;
        }
        catch (JWTCreationException e)
        {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    private UserAuth managerAuthentication(String username, String password) throws AuthenticationException
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return (UserAuth) authenticate.getPrincipal();
    }
}
