package com.example.testlogin.project_java2.security.middleware;

import com.example.testlogin.project_java2.security.JWTGeneratorToken;
import com.example.testlogin.project_java2.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthenticationFilter  extends OncePerRequestFilter {

    private final JWTGeneratorToken tokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    public JWTAuthenticationFilter(JWTGeneratorToken tokenGenerator, CustomUserDetailsService customUserDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJWTFromRequest(request);

            if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {

                String email = tokenGenerator.getEmailFromJWT(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                logger.info("JWT Filter - get request: " + authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    logger.info("User authenticated successfully");
                } else {
                    logger.warn("User authentication failed!");
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            logger.error(String.valueOf("Token expired "+ex));
            sendErrorResponse(response, "Token expired!");
        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            logger.error(String.valueOf("Invalid token! "+ex));
            sendErrorResponse(response, "Invalid token!");
        }
        catch (BadCredentialsException ex) {
            logger.error(String.valueOf("Incorrect username or password "+ex));
            sendErrorResponse(response, "Incorrect username or password!");
        } catch (Exception error){
            logger.error(String.valueOf("Error: "+error));
            sendErrorResponse(response, String.valueOf(error));
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("error", errorMessage);
        objectMapper.writeValue(response.getWriter(), map);
    }

    public String getJWTFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
