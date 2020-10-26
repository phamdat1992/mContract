package vn.inspiron.mcontract.modules.Authentication.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.inspiron.mcontract.modules.Authentication.services.UserDetailsServiceImpl;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${client.url}")
    private String url;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring()
            .antMatchers("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate",
                        "/register", "/register/verify", "/refresh-token",
                        "/log-out", "/check-account", "/forgot-password",
                        "/verify-reset-password", "/cities", "/cities/*/districts",
                        "/districts/*/wards", "/company/register").permitAll()
                .antMatchers(HttpMethod.POST,"/create-contract",
                        "/upload", "/get-data-to-sign", "/sign-document",
                        "/generate_pdf_url", "/upload_pdf",
                        "/delete_pdf", "/get-list-contract-by-condition",
                        "/bookmark", "/get-detail-contract-for-user",
                        "/update-contract-message", "/cancel-contract",
                        "/cancel-contract-by-guest").permitAll()
                .antMatchers(HttpMethod.GET, "/user-data",
                        "/account/*/transfers", "/logged").authenticated()
                .antMatchers(HttpMethod.POST, "/transfer",
                        "/account/create", "/upload", "/get-user",
                        "/sign-document", "/pdf/*").authenticated()
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(this.url));
        configuration.setAllowedHeaders(
            List.of(
                "DNT",
                "X-Mx-ReqToken",
                "Keep-Alive",
                "User-Agent",
                "X-Requested-With",
                "If-Modified-Since",
                "Cache-Control",
                "Content-Type"
            )
        );
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
