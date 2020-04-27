package br.com.matheus.kafkaline.ibgewrapper.service.estado;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.matheus.kafkaline.ibgewrapper.gateway.feing.EstadoClient;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoJson;
import feign.Feign;
import feign.gson.GsonDecoder;

@Service
public class ConsultarEstadoService {
	
	public List<EstadoJson> execute() {
		long tempoInicial = System.currentTimeMillis();
		EstadoClient github = Feign.builder()
                .decoder(new GsonDecoder())
                .target(EstadoClient.class, "https://servicodados.ibge.gov.br");
		List<EstadoJson> list = github.get();
		System.out.printf("%,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		return list;
	}
}
