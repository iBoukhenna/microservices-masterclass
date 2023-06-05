package com.dzcode.notification;

import com.dzcode.amqp.RabbitMQMessageProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	scanBasePackages = {
			"com.dzcode.notification",
			"com.dzcode.amqp",
	}
)
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}
}
