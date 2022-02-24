package com.cdm.security.config;

import com.cdm.security.model.Customer;
import com.cdm.security.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String pwd = authentication.getCredentials().toString();
    List<Customer> customers = customerRepository.findByEmail(username);
    if (customers.size() > 0){
      Customer customer = customers.get(0);
      if (passwordEncoder.matches(pwd, customer.getPwd())){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority((customer.getRole())));
        return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
      } else {
        throw new BadCredentialsException("Invalid password");
      }
    } else {
      throw new BadCredentialsException("No user registered with this details");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
