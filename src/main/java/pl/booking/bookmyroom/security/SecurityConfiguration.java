package pl.booking.bookmyroom.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.booking.bookmyroom.security.service.MyUserDetailsService;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService userDetailsService;

    public SecurityConfiguration(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
                .headers().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user").permitAll()
                .antMatchers(HttpMethod.GET, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers(HttpMethod.POST, "/corporations/login").permitAll()
                .antMatchers(HttpMethod.GET, "/logged").hasRole("USER")
            .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
            .and()
                .httpBasic();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
