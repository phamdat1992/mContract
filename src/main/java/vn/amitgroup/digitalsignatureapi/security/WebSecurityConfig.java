package vn.amitgroup.digitalsignatureapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import vn.amitgroup.digitalsignatureapi.security.jwt.JwtEntryPoint;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService userService;
    @Autowired
    private JwtEntryPoint accessDenyHandler;

    @Bean
    public JwtFilter jwtAuthenticationFilter() {
        return new JwtFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/api/file-uploads/file-s3/**").permitAll()
                .antMatchers("/api/file-uploads/file-pdf-convert/**").permitAll()
                .antMatchers("/api/comments/signer").permitAll()
                .antMatchers("/api/comments/signer/mark/**").permitAll()
                .antMatchers("/api/comments/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/companies/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/contracts/signer/**").permitAll()
                .antMatchers("/api/users/notification/signer/**").permitAll()
                .antMatchers("/api/contracts/sign/signer").permitAll()
                .antMatchers("/api/contracts/data-to-sign/signer").permitAll()
                .antMatchers("/api/comments/subscribe").permitAll()
                .antMatchers("/api/contracts/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/tags/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/users/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/file-uploads/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/authentication/logout").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .antMatchers("/api/s3-files/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                .anyRequest().permitAll()
    
                .and().exceptionHandling().authenticationEntryPoint(accessDenyHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
