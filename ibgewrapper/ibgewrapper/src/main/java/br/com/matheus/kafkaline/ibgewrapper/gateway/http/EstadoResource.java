package br.com.matheus.kafkaline.ibgewrapper.gateway.http;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.matheus.kafkaline.ibgewrapper.gateway.json.CidadeJson;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoJson;
import br.com.matheus.kafkaline.ibgewrapper.service.cidade.ConsultarCidadePorCodigoService;
import br.com.matheus.kafkaline.ibgewrapper.service.estado.ConsultarEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoResource {
	@Autowired
	private ConsultarEstadoService consultarEstadoService;
	@Autowired
	private ConsultarCidadePorCodigoService consultarCidadePorCodigoService;
	
	@GetMapping("/")
	public List<EstadoJson> consultarEstados(){
		return consultarEstadoService.execute();
	}
	@GetMapping("/{id}/cidades/")
	public List<CidadeJson> consultarCidade(@PathVariable("id") String estados){
		return consultarCidadePorCodigoService.execute(estados);
	}
}
