/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    
    @Autowired
    private ValidadorUsuaris validador;
    
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
                .requestMatchers("/admin/clients/**").hasAnyRole("ADMIN", "AGENT")
                .requestMatchers("/admin/vehicles/**").hasAnyRole("ADMIN", "AGENT")
                .requestMatchers("/admin/localitzacio/**").hasRole("ADMIN")
                .requestMatchers("/admin/agents/**").hasRole("ADMIN")
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
        .formLogin(form -> form.permitAll().defaultSuccessUrl("/admin/clients"))
        .logout(logout -> logout.logoutSuccessUrl("/public/login"));

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(validador).and().build();
    }
    
}

