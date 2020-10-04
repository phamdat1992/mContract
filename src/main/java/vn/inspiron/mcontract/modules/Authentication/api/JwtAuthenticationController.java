package vn.inspiron.mcontract.modules.Authentication.api;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.component.JwtUtils;
import vn.inspiron.mcontract.modules.Authentication.dto.JwtTokenRequestDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.JwtTokenResponseDTO;
import vn.inspiron.mcontract.modules.Authentication.services.JwtAuthenticationService;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.TimeZone;

@RestController
public class JwtAuthenticationController {

    final String ACCESS_TOKEN = "accessToken";
    final String REFRESH_TOKEN = "refreshToken";

    @Value("${jwt-access-token-time-live-in-second}")
    private String accessTokenTimeLiveInSecond;

    @Value("${jwt-refresh-token-time-live-in-second}")
    private String refreshTokenTimeLiveInSecond;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationService authenticationService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<String> createJwtAuthenticationToken(@RequestBody JwtTokenRequestDTO tokenRequest, HttpServletRequest request, HttpServletResponse response, TimeZone timeZone) {
        try {
            UserEntity user = this.authenticationService.authenticate(tokenRequest);
            JwtTokenResponseDTO accessToken = this.authenticationService.generateAccessToken(
                    String.valueOf(user.getId()),
                    String.valueOf(request.getRequestURL()),
                    timeZone,
                    Integer.parseInt(this.accessTokenTimeLiveInSecond)/60
            );

            String code = request.getHeader(HttpHeaders.USER_AGENT) + this.authenticationService.getClientIP(request);
            JwtTokenResponseDTO refreshToken = this.authenticationService.generateRefreshToken(
                    String.valueOf(user.getId()),
                    user.getPassword(),
                    code,
                    String.valueOf(request.getRequestURL()),
                    timeZone,
                    Integer.parseInt(this.refreshTokenTimeLiveInSecond)/60
            );

            HttpCookie accessTokenCookie = createCookieWithToken(
                    this.ACCESS_TOKEN,
                    accessToken.getToken(),
                    Integer.parseInt(this.accessTokenTimeLiveInSecond)
            );
            HttpCookie refreshTokenCookie = createCookieWithToken(
                    this.REFRESH_TOKEN,
                    refreshToken.getToken(),
                    Integer.parseInt(this.refreshTokenTimeLiveInSecond)
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body("Authenticated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot sign-in");
        }
    }

    @PostMapping(value = "/log-out")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, TimeZone timeZone) {
        try {
            HttpCookie accessTokenCookie = createCookieWithToken(this.ACCESS_TOKEN, "", 0);
            HttpCookie refreshTokenCookie = createCookieWithToken(this.REFRESH_TOKEN, "", 0);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body("Signed out");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot sign-out");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshJWT(HttpServletRequest request, HttpServletResponse response, TimeZone timeZone) {
        Optional<Cookie> refreshCookie = this.getRefreshTokenCookieFromRequest(request);

        if (refreshCookie.isEmpty()) {
            return ResponseEntity.badRequest().body("No refresh token");
        }

        try {
            String code = request.getHeader(HttpHeaders.USER_AGENT) + this.authenticationService.getClientIP(request);
            JwtTokenResponseDTO accessToken = this.authenticationService.refreshAccessToken(
                    code,
                    refreshCookie.get(),
                    String.valueOf(request.getRequestURL()),
                    timeZone,
                    Integer.parseInt(this.accessTokenTimeLiveInSecond)/60
            );
            HttpCookie accessTokenCookie = createCookieWithToken(
                    this.ACCESS_TOKEN,
                    accessToken.getToken(),
                    Integer.parseInt(this.accessTokenTimeLiveInSecond)
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .body("Token refreshed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot get access token");
        }
    }

    private Optional<Cookie> getRefreshTokenCookieFromRequest(HttpServletRequest request) {
        Cookie tokenCookie = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(this.REFRESH_TOKEN)) {
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
                .sameSite("strict")
                .path("/")
                .build();
    }
}
