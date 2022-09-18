package vn.amitgroup.digitalsignatureapi.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UserDetailsService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = getToken(request);

		try {
			if (jwt != null && jwtProvider.validateToken(jwt)) {
				String userAccount = jwtProvider.getUserAccount(jwt);
				UserDetails userDetails = userService.loadUserByUsername(userAccount);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}  catch (Exception ex) {
			System.out.println(ex);
		}

		filterChain.doFilter(request, response);

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
