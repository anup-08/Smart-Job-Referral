package com.smartJob.security;

import com.smartJob.jwtUtil.JwtUtil;
import com.smartJob.tokenException.AuthenticationError;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtFilterChain extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationError authenticationError;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // URIs that do not require token authentication
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/user/register") || uri.startsWith("/api/user/login") ||
                uri.startsWith("/api/user/getToken") || uri.startsWith("/api/admin/login") ||
                uri.startsWith("/api/admin/register") || uri.startsWith("/api/admin/getToken") ||
                uri.startsWith("/api/user/allJobList") || uri.startsWith("/api/admin/allJobList")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
        }

        try {
            userName = jwtUtil.extractUserName(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token, userName)) {
                    List<String> roles = jwtUtil.extractRole(token);
                    List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userName, null, grantedAuthorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            SecurityContextHolder.clearContext();
            authenticationError.commence(request, response, new InsufficientAuthenticationException("JWT expired", ex));
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            authenticationError.commence(request, response, new InsufficientAuthenticationException("Invalid token", ex));
        }

    }
}