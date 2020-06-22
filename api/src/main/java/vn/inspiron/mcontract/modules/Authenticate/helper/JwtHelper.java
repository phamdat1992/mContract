package vn.inspiron.mcontract.modules.Authenticate.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

public class JwtHelper {
    @Value("${jwt.secret-key}")
    private static String SECRET_KEY;

    public static String getSubjectFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }
}
