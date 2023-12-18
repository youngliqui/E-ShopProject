package by.youngliqui.EShopProject.config;

import by.youngliqui.EShopProject.models.Role;
import by.youngliqui.EShopProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure (HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/users").hasAnyAuthority(Role.ADMIN.name(), Role.MANAGER.name())
                        .requestMatchers("/users/new").hasAuthority(Role.ADMIN.name())
                        .anyRequest().permitAll()
        ).formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .permitAll()
                        .defaultSuccessUrl("/hello", true)
                        .failureUrl("/login?error")
        ).logout(logout ->
                logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").deleteCookies("JSESSONID")
                        .invalidateHttpSession(true)
        );

        return http.build();
    }
}
