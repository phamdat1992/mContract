package vn.inspiron.mcontract.modules.Authentication.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

public class JwtUtils {
    private static final String SECRET_KEY = "key";

    public static String getSubjectFromToken(String token)
    {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }
}
