package br.com.matheus.kafkaline.ibgeservice.gateway.json;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoList {
	private List<EstadoJson> list;
}
