package br.com.matheus.kafkaline.ibgewrapper.gateway.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.matheus.kafkaline.ibgewrapper.gateway.json.CidadeJson;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.CidadeList;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoRequestTopicJson;
import br.com.matheus.kafkaline.ibgewrapper.service.cidade.ConsultarCidadePorCodigoService;

public class CidadeListener {
	@Autowired
	private ConsultarCidadePorCodigoService consultarCidadePorCodigoService;
	@KafkaListener(topics = "${kafka.topic.request-topic-cidade}")
	public Message<String> execute(String in,@Header(KafkaHeaders.REPLY_TOPIC) byte[] replyTo,
			@Header(KafkaHeaders.CORRELATION_ID)byte[]correlation) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		long tempoInicial = System.currentTimeMillis();
		EstadoRequestTopicJson json = mapper.readValue(in, EstadoRequestTopicJson.class);
		
		List<CidadeJson> list = consultarCidadePorCodigoService.execute(json.getUf());
		
		String jsonReturn = mapper.writeValueAsString(CidadeList.builder().list(list).build());
		System.out.printf("Tempo de execução Listener Cidade: %,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		return MessageBuilder.withPayload(jsonReturn)
				.setHeader(KafkaHeaders.TOPIC, replyTo)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlation)
                .build();
	}
}
