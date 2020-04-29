package br.com.matheus.kafkaline.ibgewrapper.service.cidade;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.matheus.kafkaline.ibgewrapper.gateway.feing.CidadeClient;
import br.com.matheus.kafkaline.ibgewrapper.gateway.json.CidadeJson;
import feign.Feign;
import feign.gson.GsonDecoder;

@Service
public class ConsultarCidadePorCodigoService {
	@Cacheable(value = "cidade")
	public List<CidadeJson> execute(String estado) {
		long tempoInicial = System.currentTimeMillis();
		CidadeClient cidadeClient = Feign.builder()
				.decoder(new GsonDecoder())
				.target(CidadeClient.class, "https://servicodados.ibge.gov.br");
	List<CidadeJson> cidadesJson = cidadeClient.get(estado);
	System.out.printf("Tempo de execução Service Consultar cidade por codigo:%,3f ms%n",(System.currentTimeMillis()-tempoInicial)/1000d);
	return cidadesJson;
	}
}
