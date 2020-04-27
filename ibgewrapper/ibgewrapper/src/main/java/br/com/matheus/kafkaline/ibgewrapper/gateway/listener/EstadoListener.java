package br.com.matheus.kafkaline.ibgewrapper.gateway.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
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
	@SendTo
	public String execute() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<EstadoJson> listEstados = consultarEstadosService.execute();
		return mapper.writeValueAsString(EstadoList.builder().list(listEstados).build());
	}
}
