package br.com.matheus.kafkaline.ibgewrapper.gateway.json;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CidadeList {
	private List<CidadeJson>list;
}
