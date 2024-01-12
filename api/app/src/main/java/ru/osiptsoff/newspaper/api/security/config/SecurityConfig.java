package ru.osiptsoff.newspaper.api.security.config;

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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.security.jwt.JwtAuthenticationFilter;
import ru.osiptsoff.newspaper.api.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

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
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins(clientUrl);
			}
		};
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()
                //news
                .antMatchers(HttpMethod.GET, "/api/news/**").permitAll()
                .antMatchers("/api/news/**").hasRole("admin")
                //comments
                .antMatchers(HttpMethod.GET, "/api/comment").permitAll()
                .antMatchers("api/comment/superuser").hasRole("admin")
                .antMatchers("api/comment").hasAnyRole("user", "admin")
                //tag
                .antMatchers("/api/tag").hasRole("admin")
                //user
                .antMatchers("/api/user").hasAnyRole("user", "admin")
                //auth
                .antMatchers(HttpMethod.GET, "/api/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/**").anonymous()
                //default
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        		.build();

    }
}
