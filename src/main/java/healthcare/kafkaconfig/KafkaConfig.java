package healthcare.kafkaconfig;




	import java.util.HashMap;
	import java.util.Map;

	import healthcare.KafkaVitalEvents.VitalEvents;

	import org.apache.kafka.clients.consumer.ConsumerConfig;
	import org.apache.kafka.clients.producer.ProducerConfig;
	import org.apache.kafka.common.serialization.StringDeserializer;
	import org.apache.kafka.common.serialization.StringSerializer;

	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;

	import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
	import org.springframework.kafka.core.ConsumerFactory;
	import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
	import org.springframework.kafka.core.DefaultKafkaProducerFactory;
	import org.springframework.kafka.core.KafkaTemplate;
	import org.springframework.kafka.core.ProducerFactory;
	import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
	import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
	import org.springframework.kafka.annotation.EnableKafka;

	@Configuration
	@EnableKafka
	public class KafkaConfig {

	    // ---------- PRODUCER ----------

	    @Bean
	    public ProducerFactory<String, VitalEvents> producerFactory() {

	        Map<String, Object> config = new HashMap<>();

	        config.put(
	            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	            "localhost:9092"
	        );

	        config.put(
	            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	            StringSerializer.class
	        );

	        config.put(
	            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	            JacksonJsonSerializer.class
	        );

	        return new DefaultKafkaProducerFactory<>(config);
	    }

	    @Bean
	    public KafkaTemplate<String, VitalEvents> kafkaTemplate() {
	        return new KafkaTemplate<>(producerFactory());
	    }


	    // ---------- CONSUMER ----------

	    @Bean
	    public ConsumerFactory<String, VitalEvents> consumerFactory() {

	        Map<String, Object> config = new HashMap<>();

	        config.put(
	            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	            "localhost:9092"
	        );

	        config.put(
	            ConsumerConfig.GROUP_ID_CONFIG,
	            "vital-group"
	        );

	        config.put(
	            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	            StringDeserializer.class
	        );

	        config.put(
	            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	            JacksonJsonDeserializer.class
	        );

	        config.put(
	            "spring.json.value.default.type",
	            "healthcare.KafkaVitalEvents.VitalEvents"
	        );

	        config.put(
	            "spring.json.trusted.packages",
	            "healthcare.KafkaVitalEvents"
	        );

	        return new DefaultKafkaConsumerFactory<>(config);
	    }


	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, VitalEvents>
	    kafkaListenerContainerFactory(
	            ConsumerFactory<String, VitalEvents> consumerFactory) {

	        ConcurrentKafkaListenerContainerFactory<String, VitalEvents> factory =
	                new ConcurrentKafkaListenerContainerFactory<>();

	        factory.setConsumerFactory(consumerFactory);

	        return factory;
	    }
	}

