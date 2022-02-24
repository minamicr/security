package com.cdm.security.controller;

import com.cdm.security.model.Cards;
import com.cdm.security.model.Customer;
import com.cdm.security.repository.CardsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {
  @Autowired
  private CardsRepository cardsRepository;

  @PostMapping("/myCards")
  public List<Cards> getCardDetails(@RequestBody Customer customer) {
    List<Cards> cards = cardsRepository.findByCustomerId(customer.getId());
    if (cards != null ) {
      return cards;
    }else {
      return null;
    }
  }

}
