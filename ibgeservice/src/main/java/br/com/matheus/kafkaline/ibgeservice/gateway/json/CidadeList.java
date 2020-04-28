package br.com.matheus.kafkaline.ibgeservice.gateway.json;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CidadeList implements Serializable{
	private List<CidadeJson>list;
}
