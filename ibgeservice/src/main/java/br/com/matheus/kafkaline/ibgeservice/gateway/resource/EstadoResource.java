package br.com.matheus.kafkaline.ibgeservice.gateway.resource;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.matheus.kafkaline.ibgeservice.gateway.json.CidadeList;
import br.com.matheus.kafkaline.ibgeservice.gateway.json.EstadoList;
import br.com.matheus.kafkaline.ibgeservice.service.estado.ConsultarCidadeService;
import br.com.matheus.kafkaline.ibgeservice.service.estado.ConsultarEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoResource {
	@Autowired
	private ConsultarEstadoService consultarEstadoService;
	@Autowired
	private ConsultarCidadeService consultarCidadeService;
	@GetMapping("/")
	public EstadoList consultarEstados() throws JsonProcessingException, InterruptedException, ExecutionException{
		long tempoInicial = System.currentTimeMillis();
		EstadoList list = consultarEstadoService.execute();
		System.out.printf("Resource: Retorno estados Kafka: %,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		return list;
	}
	@GetMapping("/{id}/cidades")
	public CidadeList consultarCidades(@PathVariable("id") String estado) throws InterruptedException, ExecutionException, JsonProcessingException{
		long tempoInicial = System.currentTimeMillis();
		CidadeList list = consultarCidadeService.execute(estado);
        System.out.printf("Resource: Retorno cidades Kafka: %,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		return list;
	}
}
