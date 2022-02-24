package com.cdm.security.controller;

import com.cdm.security.model.Customer;
import com.cdm.security.model.Loans;
import com.cdm.security.repository.LoanRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {
  @Autowired
  private LoanRepository loanRepository;

  @PostMapping("/myLoans")
  public List<Loans> getLoanDetails(@RequestBody Customer customer) {
    List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customer.getId());
    if (loans != null ) {
      return loans;
    }else {
      return null;
    }
  }

}
