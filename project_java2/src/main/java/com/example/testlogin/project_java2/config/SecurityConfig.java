package com.example.testlogin.project_java2.config;


import com.example.testlogin.project_java2.security.CustomAccessDeniedHandler;
import com.example.testlogin.project_java2.security.CustomAuthenticationEntryPoint;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.security.middleware.AccountFilter;
import com.example.testlogin.project_java2.security.middleware.JWTAuthenticationFilter;
import com.example.testlogin.project_java2.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {


    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private Security security;
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    private AccountFilter accountFilter;

    @Autowired
    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter, Security security, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, AccountFilter accountFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.security = security;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.accountFilter = accountFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET,"/","/error","/login",
        "/api/v1/**")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/**")
        .permitAll()

        .antMatchers(HttpMethod.GET , "/api/user/**")
        .hasAnyRole("USER", "ADMIN")

        .antMatchers(HttpMethod.POST , "/api/user/**")
        .hasAnyRole("USER", "ADMIN")

        .antMatchers(HttpMethod.PUT , "/api/user/**")
        .hasAnyRole("USER", "ADMIN")

        .antMatchers(HttpMethod.DELETE , "/api/user/**")
        .hasAnyRole("USER", "ADMIN")


        .antMatchers(HttpMethod.GET , "/api/admin/**", "/swagger-ui/**", "/v3/**")
        .hasAnyRole("ADMIN")

        .antMatchers(HttpMethod.POST , "/api/admin/**")
        .hasAnyRole("ADMIN")

        .antMatchers(HttpMethod.PUT , "/api/admin/**")
        .hasAnyRole("ADMIN")

        .antMatchers(HttpMethod.DELETE , "/api/admin/**")
        .hasAnyRole("ADMIN")

        .antMatchers()
        .authenticated()
        .anyRequest().authenticated()
        .and()
        .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/")
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=true")
        .permitAll())
        .exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler);

        http.addFilterBefore(accountFilter,
        UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(security.authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




}
