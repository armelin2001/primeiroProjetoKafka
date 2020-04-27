package br.com.matheus.kafkaline.ibgeservice.gateway.resource;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.matheus.kafkaline.ibgeservice.gateway.json.EstadoList;
import br.com.matheus.kafkaline.ibgeservice.service.estado.ConsultarEstadoService;

public class EstadoResource {
	@Autowired
	private ConsultarEstadoService consultarEstadoService;
	@GetMapping("/")
	public EstadoList consultarEstados() throws JsonProcessingException, InterruptedException, ExecutionException{
		return consultarEstadoService.execute();
	}
}
