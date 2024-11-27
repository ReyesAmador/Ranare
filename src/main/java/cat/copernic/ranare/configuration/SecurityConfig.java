/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author reyes
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails agentUser = User.builder()
                .username("agent")
                .password(passwordEncoder().encode("agent"))
                .roles("AGENT")
                .build();

        return new InMemoryUserDetailsManager(adminUser, agentUser);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Para cifrar las contraseñas
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/clients/crear_client").hasAnyRole("ADMIN", "AGENT")
                .requestMatchers("/agents/**").hasRole("ADMIN")
                .requestMatchers("/clients").permitAll()
                .anyRequest().authenticated()
            )
        .formLogin(form -> form.permitAll().defaultSuccessUrl("/clients"))
        .logout(logout -> logout.logoutSuccessUrl("/clients"));

        return http.build();
    }
}
