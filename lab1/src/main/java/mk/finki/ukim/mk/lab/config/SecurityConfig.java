package mk.finki.ukim.mk.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity (optional, depending on your project)
                .authorizeRequests()
                .requestMatchers("/", "/home", "/assets/**", "/register").permitAll() // Public pages
                .requestMatchers("/events").authenticated() // Authenticated users can access /events
                .requestMatchers("/events/add", "/events/edit/**", "/events/delete/**").hasRole("ADMIN") // Only ADMINs can add/edit/delete events
                .anyRequest().permitAll() // All other requests are publicly accessible
                .and()
                .formLogin() // Enable default Spring Security login functionality
                .permitAll() // Allow everyone to access the login page
                .defaultSuccessUrl("/events", true) // Redirect to /events after successful login
                .and()
                .logout()
                .logoutUrl("/logout") // The logout URL
                .logoutSuccessUrl("/login"); // Redirect to /login after successful logout
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create in-memory users with roles
        var userDetailsService = new InMemoryUserDetailsManager();

        userDetailsService.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("password")) // {noop} indicates no password encoder (use a proper encoder in production)
                .roles("USER") // Assign the "USER" role
                .build());

        userDetailsService.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin123")) // {noop} indicates no password encoder (use a proper encoder in production)
                .roles("ADMIN") // Assign the "ADMIN" role
                .build());

        return userDetailsService;
    }
}
