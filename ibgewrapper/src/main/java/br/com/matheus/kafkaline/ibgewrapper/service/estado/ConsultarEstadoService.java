package br.com.matheus.kafkaline.ibgewrapper.service.estado;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.matheus.kafkaline.ibgewrapper.gateway.feing.EstadoClient;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoJson;
import feign.Feign;
import feign.gson.GsonDecoder;

@Service
public class ConsultarEstadoService {
	@Cacheable(value = "estado")
	public List<EstadoJson> execute() {
		long tempoInicial = System.currentTimeMillis();

		EstadoClient estadoCliente = Feign.builder()
                .decoder(new GsonDecoder())
                .target(EstadoClient.class, "https://servicodados.ibge.gov.br");
		
		List<EstadoJson> list = estadoCliente.get();
		
		System.out.printf("Tempo de execução Service Estado: %,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
		
		return list;
	}
}
