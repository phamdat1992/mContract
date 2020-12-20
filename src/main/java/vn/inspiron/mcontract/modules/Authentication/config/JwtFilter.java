package vn.inspiron.mcontract.modules.Authentication.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.inspiron.mcontract.modules.Authentication.model.UserAuth;
import vn.inspiron.mcontract.modules.Authentication.services.EncryptorAesGcmService;
import vn.inspiron.mcontract.modules.Authentication.services.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt-secret-key}")
    private String secretKey;

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EncryptorAesGcmService encryptorAesGcmService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException
    {
        try {
            // Manually wire UserDetailsService
            if (this.userDetailsService == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                this.userDetailsService = webApplicationContext.getBean(UserDetailsServiceImpl.class);
            }

            Cookie tokenCookie = getCookieWithAccessToken(request.getCookies());
            if (tokenCookie != null) {
                cookieAuthentication(tokenCookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chain.doFilter(request, response);
        }
    }

    private void cookieAuthentication(Cookie cookie) throws Exception {
        UsernamePasswordAuthenticationToken auth = getTokenAuthentication(cookie.getValue());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private UsernamePasswordAuthenticationToken getTokenAuthentication(String token) throws Exception {
        DecodedJWT decodedJWT = decodeAndVerifyJwt(token);
        Long userID = Long.parseLong(this.encryptorAesGcmService.decrypt(decodedJWT.getSubject()));
        UserAuth userAuth = this.userDetailsService.loadUserByID(userID);
        return new UsernamePasswordAuthenticationToken(userAuth, null, userAuth.getAuthorities());
    }

    private Cookie getCookieWithAccessToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        Cookie tokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                tokenCookie = cookie;
                break;
            }
        }

        return tokenCookie;
    }

    private DecodedJWT decodeAndVerifyJwt(String token) {
        DecodedJWT decodedJWT = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(this.secretKey)).build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            //Invalid signature/token expired
            e.printStackTrace();
        }

        return decodedJWT;
    }
}
