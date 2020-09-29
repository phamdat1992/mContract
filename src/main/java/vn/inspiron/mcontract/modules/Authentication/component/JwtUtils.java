package vn.inspiron.mcontract.modules.Authentication.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Component
public class JwtUtils {

    @Value("${jwt-secret-key}")
    private String secretKey;

    public String getSubjectFromToken(String token)
    {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }
}
