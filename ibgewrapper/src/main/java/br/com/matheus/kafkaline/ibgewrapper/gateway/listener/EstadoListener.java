package br.com.matheus.kafkaline.ibgewrapper.gateway.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoJson;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoList;
import br.com.matheus.kafkaline.ibgewrapper.service.estado.ConsultarEstadoService;

@Service
public class EstadoListener {
	@Autowired
	private ConsultarEstadoService consultarEstadosService;
	@KafkaListener(topics="${kafka.topic.request-topic}")
	public Message<String> execute(@Header(KafkaHeaders.REPLY_TOPIC) byte[] replyTo,
            @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) throws JsonProcessingException {
		long tempoInicial = System.currentTimeMillis();
	
		ObjectMapper mapper = new ObjectMapper();
		List<EstadoJson> listEstados = consultarEstadosService.execute();
		
		String jsonReturn = mapper.writeValueAsString(EstadoList.builder().list(listEstados).build());
		System.out.printf("%,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		return MessageBuilder.withPayload(jsonReturn)
				.setHeader(KafkaHeaders.TOPIC, replyTo)
				.setHeader(KafkaHeaders.CORRELATION_ID, correlation)
				.build();
	
	}
}
