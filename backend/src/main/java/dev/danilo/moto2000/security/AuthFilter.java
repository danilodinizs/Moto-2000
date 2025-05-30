package dev.danilo.moto2000.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils utils;
    private final AuthService service;

    private static final String JWT_COOKIE_NAME = "jwt-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        if(token != null) {
            try {
                String username = utils.extractUsername(token);

                if(StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = service.loadUserByUsername(username);

                    if(utils.isTokenValid(token, userDetails)) {
                        log.info("Token is valid - User: {}", username);

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                log.warn("JWT Token processing error: {}", e.getMessage());
                // Continue without authentication
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occured in AuthFilter: {}", e.getMessage());
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String tokenWithBearer = request.getHeader("Authorization");
        if(tokenWithBearer != null && tokenWithBearer.startsWith("Bearer ")) {
            log.debug("Token extracted from Authorization header");
            return tokenWithBearer.substring(7);
        }

        String tokenFromCookie = extractTokenFromCookie(request, JWT_COOKIE_NAME);
        if(tokenFromCookie != null) {
            log.debug("Token extracted from cookie");
            return tokenFromCookie;
        }

        return null;
    }

    private String extractTokenFromCookie(HttpServletRequest req, String name) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (name.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
