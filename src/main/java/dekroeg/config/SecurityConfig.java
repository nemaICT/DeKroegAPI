package dekroeg.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import dekroeg.service.UserAuthorizationDetailsService;


// for follow alone SecurityConfig see: https://www.youtube.com/watch?v=jwiCYyBwugo
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthorizationDetailsService userAuthorizationDetailsService;

    /**
     * BasicAuthenticationFilter --> will try to find the basic authenticationHeader on the request,
     * once that is found it will authenticate the header + user + password
     *
     * UsernamePasswordAuthenticationFilter --> Processes an authentication form submission. Called AuthenticationProcessingFilter prior to Spring Security 3.0.
     * Login forms must present two parameters to this filter: a username and password.
     *
     * DefaultLoginPageGeneratingFilter --> will be used to generate default "login form" in the case where a user doesn't configure a login page.
     * It has a private generateLoginPageHtml responsible for generating the default html view.
     *
     * DefaultLogoutPageGeneratingFilter --> will be used to generate default "logout form" in the case where a user doesn't configure a login page.
     * It has a private generateLoginPageHtml responsible for generating the default html view.
     *
     * FilterSecurityInterceptor -->  is responsible for handling the security of HTTP resources.
     * It requires a reference to an AuthenticationManager and an AccessDecisionManager.
     * It is also supplied with configuration attributes that apply to different HTTP URL request
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // https://www.youtube.com/watch?v=RCZq6FDsqkE
        http.csrf()
                .disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
             .authorizeRequests()
             .anyRequest().authenticated()
             .and()
             .formLogin()
             .and()
             .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Testing encoder password {}",passwordEncoder.encode("nema"));

        auth.inMemoryAuthentication()
                .withUser("erik")
                .password(passwordEncoder.encode("erik"))
                .roles("USER")
                .and()
                .withUser("nema")
                .password(passwordEncoder.encode("nema"))
                .roles("USER", "ADMIN");


        // see: https://www.youtube.com/watch?v=mRlSMK-n10M
        auth.userDetailsService(userAuthorizationDetailsService)
                .passwordEncoder(passwordEncoder);

    }
}
