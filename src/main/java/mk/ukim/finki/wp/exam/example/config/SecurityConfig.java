package mk.ukim.finki.wp.exam.example.config;

import mk.ukim.finki.wp.exam.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        // TODO: If you are implementing the security requirements, remove this following line
//        //     web.ignoring().antMatchers("/**");
//    }

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().hasRole("ADMIN")
                .and()
                .formLogin()
                .permitAll()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/products", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/logout");

        return http.build();
    }


    @Bean
    public AuthenticationManager authManager(AuthenticationManagerBuilder authBuilder, PasswordEncoder passwordEncoder) throws Exception {
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authBuilder.build();
    }


}


