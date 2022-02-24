package com.cdm.security.controller;

import com.cdm.security.model.AccountTransactions;
import com.cdm.security.model.Customer;
import com.cdm.security.repository.AccountTransactionsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

  @Autowired
  private AccountTransactionsRepository accountTransactionsRepository;

  @PostMapping("/myBalance")
  public List<AccountTransactions> getBalanceDetails(@RequestBody Customer customer) {
    List<AccountTransactions> accountTransactions = accountTransactionsRepository.
        findByCustomerIdOrderByTransactionDtDesc(customer.getId());
    if (accountTransactions != null ) {
      return accountTransactions;
    }else {
      return null;
    }
  }

}
