package com.dzcode.customer;

import com.dzcode.amqp.RabbitMQMessageProducer;
import com.dzcode.clients.fraud.FraudCheckResponse;
import com.dzcode.clients.fraud.FraudClient;
import com.dzcode.clients.notification.NotificationClient;
import com.dzcode.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RabbitMQMessageProducer rabbitMQMessageProducer, FraudClient fraudClient) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email()).build();
        // todo : check if valid and not taken email
        // todo : check if fraudster
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        // todo : send notification
        NotificationRequest notificationRequest = new NotificationRequest(
            customer.getId(),
            customer.getEmail(),
            String.format("Hi %s, welcome to Microservices...",
                    customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

}
