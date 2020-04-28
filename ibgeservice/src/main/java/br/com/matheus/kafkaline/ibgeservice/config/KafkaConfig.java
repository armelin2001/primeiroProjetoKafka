package br.com.matheus.kafkaline.ibgeservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
	// site de estudo apache kafka 
	//https://callistaenterprise.se/blogg/teknik/2018/10/26/synchronous-request-reply-over-kafka/
	/*ANOTATIONS QUE SÃO USADAS NAS CLASSES PARA USAR O KAFKA:
	 *@SendTo==> Anotation responsavel por fazer a replycação da mensagem de requisição, logo quando essa mensagem recebe uma resposta do server, o objeto dela vai
	 *ser retornado pelo metodo listener que vai automaticamente encapsular a resposta e mandar no replay message*/
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.requestreply-topic}")
    private String requestReplyTopic;

    @Value("${kafka.topic.requestreply-topic-cidade}")
    private String requestReplyTopicCidade;

    @Value("${kafka.consumergroup}")
    private String consumerGroup;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        return props;
    }
    /*Para o ReplyingKafkaTemplate conseguir fazer as operações de replicação de request,
     *precisamos usar 2 factories esses são:
     *ProducerFactory, responsavel por criar as requisições do templete;
     *ReplyConsumerFactory, responsavel por consumir as requisições de replicação;
     *E para armazenar os requests precisamos do MessageListenerContainer*/
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    // formulario basico que vai ficar no lado do cliente para poder fazer o reply
    //nesse formulario vemos ter a mensagem de request com as strings keys
    
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> pf, KafkaMessageListenerContainer<String, String> container) {
        ReplyingKafkaTemplate template = new ReplyingKafkaTemplate<>(pf, container);
        template.setReplyTimeout(60000L);
        return template;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new StringDeserializer());
    }
    
    
    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
        ContainerProperties containerProperties = new ContainerProperties(requestReplyTopic, requestReplyTopicCidade);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }
    /*usando só uma repartição para mandar a requisição para o kafka, se fossemos usar multiplas repartições para envio de request precisariamos separalas em 
     * topicos na classe de configuração kafkaListenerContainerFactory, e para a classe saber a qual partição essa requisição pertence precisamos identificar a 
     * quais topicos elas pertencem*/
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }
}
