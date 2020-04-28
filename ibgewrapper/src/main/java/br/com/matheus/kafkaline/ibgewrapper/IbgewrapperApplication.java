package br.com.matheus.kafkaline.ibgewrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableKafka
public class IbgewrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbgewrapperApplication.class, args);
	}

}
