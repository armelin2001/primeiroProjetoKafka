package br.com.matheus.kafkaline.ibgeservice.service.estado;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.matheus.kafkaline.ibgeservice.gateway.json.CidadeList;
import br.com.matheus.kafkaline.ibgeservice.gateway.json.EstadoRequestTopicJson;

@Service
public class ConsultarCidadeService {
	//templete kafka que vai armazenar os parametros de request e replay
	@Autowired
	private ReplyingKafkaTemplate<String,String,String> kafkaTemplate;
	
	//fila que vai carregar os dados para o outro microservice
    @Value("${kafka.topic.request-topic-cidade}")
    private String requestTopic;

	//fila que vai ser aguardado a resposta
    @Value("${kafka.topic.requestreply-topic-cidade}")
    private String requestReplyTopic;
    
    @Cacheable(value = "cidade-principal")
	public CidadeList execute(String estado) throws JsonProcessingException, ExecutionException, InterruptedException {

        long tempoInicial = System.currentTimeMillis();

        // convertendo obj para string
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(EstadoRequestTopicJson.builder().uf(estado).build());

        // montando o producer que ira ser enviado para o kafka
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(requestTopic, jsonString);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));

        // enviando
        RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // recebendos o retorno
        SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

        CidadeList listJsonRetorn = mapper.readValue(consumerRecord.value(), CidadeList.class);

        System.out.printf("Retorno das cidades pelo kafka: %.3f ms%n", (System.currentTimeMillis() - tempoInicial) / 1000d);

        return listJsonRetorn;
    }
}
