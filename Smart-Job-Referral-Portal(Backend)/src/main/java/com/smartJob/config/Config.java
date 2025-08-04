package com.smartJob.config;

import com.smartJob.security.JwtFilterChain;
import com.smartJob.tokenException.AccessDeniedError;
import com.smartJob.tokenException.AuthenticationError;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class Config  {

    private final AccessDeniedError accessDeniedError;
    private final AuthenticationError authenticationError;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> cors.configurationSource(configurationSource()))
                .authorizeHttpRequests((response)->
                    response.requestMatchers(
                                    "/api/user/login",
                                    "/api/user/register",
                                    "/api/user/getToken",
                                    "/api/admin/login", "/api/user/searchJob",
                                    "/api/admin/register",
                                    "/api/admin/getToken", "/api/user/allJobList" , "/api/admin/allJobList"
                            ).permitAll()
                            .requestMatchers("/api/user/referral/**","/api/user/referralData/*","/api/user/updateDoc/**" ,"/api/user/getUser" ,"/api/user/getReferralDetails").hasRole("USER")
                            .requestMatchers("/api/admin/addJob" ,"/api/admin/referralData/*",
                                    "/api/admin/seeReferral" ,"/api/admin/getPostedJobs",
                                    "/api/admin/seeReferral/*","/api/user/getUser","/api/admin/searchJob"
                                    ,"/api/admin/delete/{id}" , "/api/admin/getPostedJobReferral",
                                    "/api/admin/setStatus/*")
                            .hasRole("ADMIN")
                            .anyRequest().permitAll())
                .exceptionHandling((ex)->ex.authenticationEntryPoint(authenticationError)
                        .accessDeniedHandler(accessDeniedError));

        http.csrf(AbstractHttpConfigurer::disable);


        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilterChain , UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    private final JwtFilterChain jwtFilterChain;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**" , configuration);
        return  source;
    }
}
