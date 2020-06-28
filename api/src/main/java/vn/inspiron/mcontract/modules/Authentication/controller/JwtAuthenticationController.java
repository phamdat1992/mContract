package vn.inspiron.mcontract.modules.Authentication.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.model.JwtTokenRequest;
import vn.inspiron.mcontract.modules.Authentication.model.JwtTokenResponse;
import vn.inspiron.mcontract.modules.Authentication.model.JwtUtils;
import vn.inspiron.mcontract.modules.Authentication.services.JwtAuthenticationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private Environment env;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationService authenticationService;

    @RequestMapping(value="/test")
    public String test() {
        return "{\"name\":\""+ env.getProperty("jwt-secret-key") +"\",\"age\":30,\"city\":\"New York\"}";
    }

    @RequestMapping(value="/logged")
    public String logged() {
        return "{\"name\":\"aaa\",\"age\":30,\"city\":\"New York\"}";
    }

    @PostMapping(value="/authenticate")
    public ResponseEntity<String> createJwtAuthenticationToken(@RequestBody JwtTokenRequest tokenRequest, HttpServletRequest request, HttpServletResponse response, TimeZone timeZone)
    {
        System.out.println("testxxx");
        System.out.println(passwordEncoder.encode("test"));
        try
        {

            JwtTokenResponse accessToken = this.authenticationService.authenticate(tokenRequest, String.valueOf(request.getRequestURL()), timeZone);
            JwtTokenResponse refreshToken = authenticationService.generateRefreshToken(tokenRequest.getUsername(), String.valueOf(request.getRequestURL()), timeZone);


            HttpCookie accessTokenCookie = createCookieWithToken("accessToken", accessToken.getToken(), 10 * 60);
            HttpCookie refreshTokenCookie = createCookieWithToken("refreshToken", refreshToken.getToken(), 60 * 60);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body("Authenticated");
        }
        catch (AuthenticationException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshJWT(HttpServletRequest request, HttpServletResponse response, TimeZone timeZone)
    {
        Optional<Cookie> refreshCookie = getRefreshTokenCookieFromRequest(request);

        if (refreshCookie.isEmpty())
        {
            return ResponseEntity.badRequest().body("No refresh token");
        }

        try
        {
            JwtTokenResponse accessToken = authenticationService.refreshAccessToken(refreshCookie.get(), String.valueOf(request.getRequestURL()), timeZone);

            HttpCookie accessTokenCookie = createCookieWithToken("accessToken", accessToken.getToken(), 10 * 60);
            HttpCookie refreshTokenCookie = createCookieWithToken("refreshToken",
                    authenticationService.generateRefreshToken(JwtUtils.getSubjectFromToken(accessToken.getToken()), request.getRequestURI(), timeZone).getToken(),
                    60 * 60);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body("Token refreshed");
        } catch (JWTVerificationException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private Optional<Cookie> getRefreshTokenCookieFromRequest(HttpServletRequest request)
    {
        Cookie tokenCookie = null;

        if (request.getCookies() != null)
        {
            for (Cookie cookie : request.getCookies())
            {
                if (cookie.getName().equals("refreshToken"))
                {
                    tokenCookie = cookie;
                    break;
                }
            }
        }
        return Optional.ofNullable(tokenCookie);
    }

    private HttpCookie createCookieWithToken(String name, String token, int maxAge) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)
                .maxAge(maxAge)
                .path("/")
                .build();
    }
}
