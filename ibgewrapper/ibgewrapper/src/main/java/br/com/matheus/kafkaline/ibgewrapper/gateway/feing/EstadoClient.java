package br.com.matheus.kafkaline.ibgewrapper.gateway.feing;

import java.util.List;

import br.com.matheus.kafkaline.ibgewrapper.gateway.json.EstadoJson;
import feign.RequestLine;

public interface EstadoClient {
	// api para consulta de estados pelo ibge
	// https://servicodados.ibge.gov.br/api/v1/localidades/estados
	@RequestLine("GET /api/v1/localidades/estados")
	List<EstadoJson> get();
}
