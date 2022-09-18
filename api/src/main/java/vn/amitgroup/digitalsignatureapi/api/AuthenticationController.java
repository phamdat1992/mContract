package vn.amitgroup.digitalsignatureapi.api;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.amitgroup.digitalsignatureapi.dto.JwtResponse;
import vn.amitgroup.digitalsignatureapi.dto.LoginForm;
import vn.amitgroup.digitalsignatureapi.dto.RefreshTokenForm;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.entity.RefreshToken;
import vn.amitgroup.digitalsignatureapi.entity.TokenIsDestroy;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.OtpCodeService;
import vn.amitgroup.digitalsignatureapi.service.RefreshTokenService;
import vn.amitgroup.digitalsignatureapi.service.TokenIsDestroyService;
import vn.amitgroup.digitalsignatureapi.service.UserService;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @Value("${jwt.expiration}")
	private int jwtExpiration;
    @Value("${my.domain}")
	private String myDomain;
    private final String SIGNIN_TYPE = "SIGNIN13@98qt";
    @Autowired
    private OtpCodeService codeService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenIsDestroyService tokenIsDestroyService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtResponse>> login(@RequestBody @Valid LoginForm loginForm) {
        loginForm.setEmail(loginForm.getEmail().toLowerCase());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getOtp()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        codeService.clearOTP(SIGNIN_TYPE+loginForm.getEmail());
        ResponseCookie resCookie = ResponseCookie.from("Authorization", "Bearer_"+jwt)
            .httpOnly(true)
            .sameSite("None")
            .secure(true)
            .path("/")
            .domain(myDomain)
            .maxAge(7 * 24 * 60 * 60)
            .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(new BaseResponse<>(
                        new JwtResponse(jwt,refreshToken.getId(), user.getEmail(), user.getFullName(), user.getRole().name(),new Date(new Date().getTime()+jwtExpiration * 1000)),
                        HttpStatus.OK.value()));

    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<JwtResponse>> save(@RequestBody @Valid LoginForm loginForm) throws ApiException,ErrorCodeException{
        loginForm.setEmail(loginForm.getEmail().toLowerCase());
        if (Integer.parseInt(loginForm.getOtp()) == codeService.getOtp(SIGNIN_TYPE + loginForm.getEmail())) {
            userService.save(loginForm.getEmail());
        }
        else{
            throw ErrorCodeException.GetCodeExpired();
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getOtp()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        codeService.clearOTP(SIGNIN_TYPE+loginForm.getEmail());
        ResponseCookie resCookie = ResponseCookie.from("Authorization", "Bearer_"+jwt)
            .httpOnly(true)
            .sameSite("None")
            .secure(true)
            .path("/")
            .domain(myDomain)
            .maxAge(7 * 24 * 60 * 60)
            .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString())
            .body(new BaseResponse<>(
                    new JwtResponse(jwt,refreshToken.getId(), user.getEmail(), user.getFullName(), user.getRole().name(),new Date(new Date().getTime()+jwtExpiration * 1000)),
                    HttpStatus.OK.value()));
    }

    @GetMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String token = getToken(request);
            Date date = jwtProvider.getIssuedAt(token);
            TokenIsDestroy  tokenIsDestroy= new TokenIsDestroy();
            tokenIsDestroy.setToken(token);
            tokenIsDestroy.setExpirationTime(date); 
            tokenIsDestroyService.add(tokenIsDestroy);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PostMapping("/refreshtoken")
	public ResponseEntity<BaseResponse<JwtResponse>> refreshtoken(@Valid @RequestBody RefreshTokenForm refreshTokenForm) throws Exception {
        RefreshToken refreshToken =refreshTokenService.findByToken(refreshTokenForm.getRefreshToken());
        User user =refreshToken.getUser();
		String token = jwtProvider.doGenerateRefreshToken(user.getEmail());
        refreshToken=refreshTokenService.createRefreshToken(user);
        refreshTokenService.deleteByToken(refreshTokenForm.getRefreshToken());
        ResponseCookie resCookie = ResponseCookie.from("Authorization", "Bearer_"+token)
        .httpOnly(true)
        .sameSite("None")
        .secure(true)
        .path("/")
        .domain(myDomain)
        .maxAge(7 * 24 * 60 * 60)
        .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString())
        .body(new BaseResponse<>(
                new JwtResponse(token,refreshToken.getId(), user.getEmail(), user.getFullName(), user.getRole().name(),new Date(new Date().getTime()+jwtExpiration * 1000)),
                HttpStatus.OK.value()));
	}


    private String getToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("Authorization".equals(cookie.getName())) {
					String accessToken = cookie.getValue();
					return accessToken.replace("Bearer_", "");
				}
			}
		}
		return null;

	}

}
