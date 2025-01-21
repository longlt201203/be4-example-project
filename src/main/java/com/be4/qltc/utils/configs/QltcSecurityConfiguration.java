
package com.be4.qltc.utils.configs;

import com.be4.qltc.utils.QltcAuthority;
import com.be4.qltc.utils.QltcSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class QltcSecurityConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public QltcSecurityFilter qltcSecurityFilter() {
        return new QltcSecurityFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/profile")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/accounts/")
                        .permitAll()
                        .requestMatchers("/api/accounts/**")
                        .hasAnyAuthority(QltcAuthority.of(1).getAuthority())
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .denyAll()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(qltcSecurityFilter(), AuthorizationFilter.class);
        return http.build();
    }
}
