package br.com.matheus.kafkaline.ibgewrapper.gateway.feing;

import java.util.List;

import br.com.matheus.kafkaline.ibgewrapper.gateway.json.CidadeJson;
import feign.Param;
import feign.RequestLine;

public interface CidadeClient {
	//https://servicodados.ibge.gov.br/api/v1/localidades/estados/{UF}/municipios
	//vai dar um get nas cidades pelo atributo uf no url
	@RequestLine("GET /api/v1/localidades/estados/{UF}/municipios")
	List<CidadeJson> get(@Param("UF") String uf);

}
