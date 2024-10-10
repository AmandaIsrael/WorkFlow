package com.WorkFlow.config;

import com.WorkFlow.dto.RequestResponseWrapper;
import com.WorkFlow.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        RequestResponseWrapper HttpWrapper = new RequestResponseWrapper(request, response);
        final String authorizationHeader = HttpWrapper.request().getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("Authorization header is missing or does not start with 'Bearer'");
            filterChain.doFilter(HttpWrapper.request(), HttpWrapper.response());
            return;
        }

        final String jwtToken = authorizationHeader.substring(BEARER_PREFIX.length());
        final String subject = jwtService.extractSubject(jwtToken); // email or username
        boolean userIsNotAuthenticated = SecurityContextHolder.getContext().getAuthentication() == null;

        if (subject != null && userIsNotAuthenticated) {
            authenticateUser(HttpWrapper, subject, jwtToken);
        }

        filterChain.doFilter(HttpWrapper.request(), HttpWrapper.response());
    }

    private void authenticateUser(
            RequestResponseWrapper HttpWrapper,
            String subject,
            String jwtToken
    ) throws IOException {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);
        String username = userDetails.getUsername();

        try {
            boolean isTokenValid = jwtService.isTokenValid(jwtToken, userDetails);

            if (isTokenValid){
                logger.info("User {} is authenticated", username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // without credentials when is a new user
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(HttpWrapper.request())
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (InvalidTokenException e) {
            logger.error("Invalid JWT token for user {}: {}", username, e.getMessage());
            HttpServletResponse response = HttpWrapper.response();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}