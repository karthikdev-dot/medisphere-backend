
package healthcare.jwtfilter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import healthcare.Loadbyusername.LoadbyUsername;
import healthcare.jwt.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWT JWTtoken;

    @Autowired
    private LoadbyUsername loadusername;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get requested URL
        String path = request.getServletPath();

        System.out.println("Request path = " + path);

        // =========================================
        // REGISTER AND LOGIN DO NOT NEED JWT
        // =========================================

        if (path.equals("/api/reg") ||
            path.equals("/auth/login") ||
            path.startsWith("/api/vitals/")||
            path.startsWith("/api/medications/") ||
            path.startsWith("/api/patients/")||
            path.startsWith("/api/labrecords/")||
            path.startsWith("/api/auditlog")){

            System.out.println("Public endpoint - JWT skipped");

            filterChain.doFilter(request, response);
            return;
        }

        // =========================================
        // GET AUTHORIZATION HEADER
        // =========================================

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // =========================================
        // CHECK BEARER TOKEN
        // =========================================

        if (authHeader != null &&
            authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {

                username = JWTtoken.extractUsername(token);

                System.out.println(
                        "Username from JWT = " + username
                );

            } catch (Exception e) {

                System.out.println(
                        "Invalid JWT token"
                );
            }
        }

        // =========================================
        // LOAD USER
        // =========================================

        if (username != null &&
            SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

            UserDetails userDetails =
                    loadusername.loadUserByUsername(username);

            // =====================================
            // VALIDATE TOKEN
            // =====================================

            boolean valid =
                    JWTtoken.validateToken(
                            token,
                            userDetails
                    );

            System.out.println(
                    "Token valid = " + valid
            );

            if (valid) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

                System.out.println(
                        "User authenticated successfully"
                );
            }
        }

        // =========================================
        // CONTINUE REQUEST
        // =========================================

        filterChain.doFilter(request, response);
    }
}