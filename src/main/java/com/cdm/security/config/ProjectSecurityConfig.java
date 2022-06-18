package com.cdm.security.config;

import com.cdm.security.filter.AuthoritiesLoggingAfterFilter;
import com.cdm.security.filter.AuthoritiesLoggingAtFilter;
import com.cdm.security.filter.JWTTokenGeneratorFilter;
import com.cdm.security.filter.JWTTokenValidatorFilter;
import com.cdm.security.filter.RequestValidationBeforeFilter;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        /* JWT - Diz ao spring security para não gerenciar token,
          a aplicação ficará responsável
         */
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors().configurationSource(getCorsConfiguration())
        .and()
        /* JWT - Desabilitado csrf para utilização do JWT
        .csrf()
        .ignoringAntMatchers("/contact")
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
         */
        .csrf().disable()
        ////.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
        ////.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
        ////.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
        .authorizeRequests()
        /* Desabilitada verificação de authorities por roles
        .antMatchers("/myAccount").hasAuthority("WRITE")
        .antMatchers("/myBalance").hasAuthority("READ")
        .antMatchers("/myLoans").hasAuthority("DELETE")
         */
        .antMatchers("/myAccount").hasRole("USER")
        .antMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
        .antMatchers("/myLoans").authenticated()
        .antMatchers("/myCards").authenticated()
        .antMatchers("/notices").permitAll()
        .antMatchers("/contact").permitAll()
        .and()
        .httpBasic();
        /* JWT - Desabilitado
        .and()
        .formLogin()
        .and()
        .httpBasic();

         */
  }

  private CorsConfigurationSource getCorsConfiguration(){
     return new CorsConfigurationSource() {
      @Override
      public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        /* JWT - Expor o header Authorization para o front */
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setMaxAge(3600L);
        return config;

      }
    };

  }
/*

    http.authorizeRequests()
        .anyRequest()
        .denyAll()
        .and()
        .formLogin()
        .and()
        .httpBasic();


    http.authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .formLogin()
        .and()
        .httpBasic();
  }
*/

/*
  protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password("12345")
        .authorities("admin")
        .and()
        .withUser("user")
        .password("12345")
        .authorities("read")
        .and()
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
  }
*/

/*
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
    JdbcUserDetailsManager
    UserDetails user = User.withUsername("admin").password("12345").authorities("admin").build();
    UserDetails user1 = User.withUsername("user").password("12345").authorities("read").build();
    userDetailsManager.createUser(user);
    userDetailsManager.createUser(user1);
    auth.userDetailsService(userDetailsManager);
  }
*/

/*
  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }
*/

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
