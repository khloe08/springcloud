package com.spring_cloud.eureka.client.auth.security;


import com.spring_cloud.eureka.client.auth.application.RedisService;
import com.spring_cloud.eureka.client.auth.security.jwt.JwtAuthenticationFilter;
import com.spring_cloud.eureka.client.auth.security.jwt.JwtProperties;
import com.spring_cloud.eureka.client.auth.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//preAuthorize, postAuthorize 사용 어노테이션
@EnableMethodSecurity
public class AuthSecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final RedisService redisService;



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, jwtProperties, redisService, authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/signIn");

        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers("/auth/signIn").permitAll()
                .requestMatchers("/auth/signUp").permitAll()
                .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html",
                    "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}