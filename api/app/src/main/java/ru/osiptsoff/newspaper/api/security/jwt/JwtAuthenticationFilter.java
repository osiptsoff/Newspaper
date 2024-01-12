package ru.osiptsoff.newspaper.api.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.TextMessageDto;
import ru.osiptsoff.newspaper.api.model.auth.UserPrincipal;

class JwtAuthenticationException extends Exception {
    
}

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtility jwtUtility;
    private final ObjectMapper jsonObjectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authentication");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring("Bearer ".length());

        UserPrincipal userPrincipal = jwtUtility.parseAndValidateAccessToken(token);
        if(userPrincipal == null) {
            TextMessageDto textMessage = new TextMessageDto("invalid access token");

            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response
                .getWriter()
                .write( jsonObjectMapper.writeValueAsString(textMessage) );
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
