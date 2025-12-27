

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity /* <--- Must be Reactive for API Gateway */
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // New Lambda DSL syntax
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/products/**", "/api/orders/**").authenticated() // Secure these paths
                .anyExchange().permitAll() // Allow everything else (like /eureka or login)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {}) // Enable JWT validation
            );
            
        return http.build();
    }
}