package com.cdm.security.config;

import com.cdm.security.model.Customer;
import com.cdm.security.model.SecurityCustomer;
import com.cdm.security.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<Customer> customer = customerRepository.findByEmail(username);
    if (customer.size() == 0) {
      throw new UsernameNotFoundException("User details not found for the user : " + username);
    }
    return new SecurityCustomer(customer.get(0));
  }
}
