package com.proyecto.gateway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		
		http
	        .csrf(csrf -> csrf.disable())
	        .cors(cors -> cors.configurationSource(request -> {
	            CorsConfiguration corsConfiguration = new CorsConfiguration();
	            corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
	            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	            corsConfiguration.setAllowCredentials(true);
	            return corsConfiguration;
	        }))
	        .authorizeExchange((authorize) -> authorize
		            .pathMatchers(HttpMethod.OPTIONS ,"/**").permitAll()
		            .pathMatchers("/api/login/**").permitAll()
		            
		            .pathMatchers("/api/usuarios/**").hasRole("ADMIN")
		            
		            .pathMatchers(HttpMethod.GET, "/api/habitaciones/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers("/api/habitaciones/**").hasRole("ADMIN")
		    		
		    		.pathMatchers(HttpMethod.GET, "/api/huespedes/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.POST, "/api/huespedes/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.PUT, "/api/huespedes/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.DELETE, "/api/huespedes/**").hasRole("ADMIN")
		    		
		    		.pathMatchers(HttpMethod.GET, "/api/reservas/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.POST, "/api/reservas/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.PUT, "/api/reservas/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.PATCH, "/api/reservas/**").hasAnyRole("ADMIN", "USER")
		    		.pathMatchers(HttpMethod.DELETE, "/api/reservas/**").hasRole("ADMIN")

		            .anyExchange().authenticated()
		        )
	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
	            jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter())));
	
	    return http.build();
	}
	
	@Bean
	ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}
}