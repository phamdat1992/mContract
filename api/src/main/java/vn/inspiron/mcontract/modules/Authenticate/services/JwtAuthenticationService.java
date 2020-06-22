package vn.inspiron.mcontract.modules.Authenticate.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import vn.inspiron.mcontract.modules.Authenticate.model.jwt.JwtTokenRequest;
import vn.inspiron.mcontract.modules.Authenticate.model.jwt.JwtTokenResponse;

import javax.servlet.http.Cookie;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class JwtAuthenticationService {
    private AuthenticationManager authenticationManager;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.token-time-live}")
    private String TOKEN_TIME_LIVE;

    public JwtAuthenticationService(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    public JwtTokenResponse authenticate(JwtTokenRequest tokenRequest, String url, TimeZone timeZone) throws AuthenticationException
    {
        UserDetails userDetails = managerAuthentication(tokenRequest.getUsername(), tokenRequest.getPassword());

        String token = generateToken(userDetails.getUsername(), url, timeZone);

        return new JwtTokenResponse(token);
    }

    public JwtTokenResponse generateRefreshToken(String subject, String url, TimeZone timeZone)
    {
        try
        {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = JWT.create()
                    .withIssuer(url)
                    .withSubject(subject)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(Integer.parseInt(TOKEN_TIME_LIVE)).toInstant()))
                    .sign(algorithm);

            return new JwtTokenResponse(token);
        }
        catch (JWTCreationException e)
        {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    public JwtTokenResponse refreshAccessToken(Cookie cookie, String url, TimeZone timeZone) throws JWTVerificationException
    {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(cookie.getValue());

        return new JwtTokenResponse(generateToken(decodedJWT.getSubject(), url, timeZone));
    }

    private String generateToken(String username, String url, TimeZone timeZone)
    {
        try
        {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = JWT.create()
                    .withIssuer(url)
                    .withSubject(username)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(10).toInstant()))
                    .sign(algorithm);

            return token;
        }
        catch (JWTCreationException e)
        {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    private UserDetails managerAuthentication(String username, String password) throws AuthenticationException
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return (UserDetails) authenticate.getPrincipal();
    }
}
