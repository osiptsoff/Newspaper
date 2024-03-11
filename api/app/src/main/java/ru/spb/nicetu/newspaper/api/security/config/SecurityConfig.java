package ru.spb.nicetu.newspaper.api.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.spb.nicetu.newspaper.api.security.jwt.JwtAuthenticationFilter;
import ru.spb.nicetu.newspaper.api.service.UserServiceImpl;

/**
 * <p>Spring Security configuration class.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;

    @Setter
    @Value("${app.config.security.clientUrl}")
    private String clientUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(clientUrl);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors().and()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .authorizeRequests()
            //news
            .antMatchers(HttpMethod.GET, "/news/**").permitAll()
            .antMatchers("/news/**").hasAuthority("admin")
            //comments
            .antMatchers(HttpMethod.GET, "/comment").permitAll()
            .antMatchers("/comment/superuser").hasAuthority("admin")
            .antMatchers("/comment").hasAnyAuthority("user", "admin")
            //tag
            .antMatchers(HttpMethod.GET, "/tag").permitAll()
            .antMatchers("/tag").hasAuthority("admin")
            //user
            .antMatchers("/user/**").hasAnyAuthority("user", "admin")
            //auth
            .antMatchers(HttpMethod.GET, "/auth").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/**").anonymous()
            //default
            .antMatchers(HttpMethod.GET, "/**").permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
