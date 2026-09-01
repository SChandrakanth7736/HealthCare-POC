package com.theelixrlabs.healthcare.config;

import com.theelixrlabs.healthcare.constants.SecurityConstants;
import com.theelixrlabs.healthcare.utility.AuthenticationEntryPointUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for securing endpoints
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String JWK_SET_URI;
    private final Environment environment;
    private final AuthenticationEntryPointUtility authenticationEntryPointUtility;

    public SecurityConfig(@Value(SecurityConstants.GOOGLE_JWK_SET_URI) String jwkSetUri,
                          Environment environment, AuthenticationEntryPointUtility authenticationEntryPointUtility) {
        JWK_SET_URI = jwkSetUri;
        this.environment = environment;
        this.authenticationEntryPointUtility = authenticationEntryPointUtility;
    }

    /**
     * This bean configures spring security with Google Authentication based on profiles
     *
     * @param httpSecurity HttpSecurity instance
     * @return SecurityFilterChain instance
     * @throws Exception if any exception occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String activeProfile = environment.getProperty(SecurityConstants.ACTIVE_PROFILE);
        if (!activeProfile.equals(SecurityConstants.DEV_PROFILE_CONSTANT)) {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> authorize
                            .anyRequest().authenticated())
                    .oauth2Login(withDefaults())
                    .oauth2Client(withDefaults())
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(withDefaults())
                            .authenticationEntryPoint(authenticationEntryPointUtility));
            return httpSecurity.build();
        }
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll());
        return httpSecurity.build();
    }

    /**
     * Bean to decode and validate JWT tokens
     *
     * @return JWTDecoder object
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(JWK_SET_URI).build();
    }
}
