package com.dzcode.customer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email()).build();
        // todo : check if valid and not taken email
        // todo : check if fraudster
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http://FRAUD/api/v1/fraud-check/{customerId}"
                , FraudCheckResponse.class, customer.getId());
        // todo : send notification
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

    }

}
