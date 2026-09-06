package healthcare.KafkaProducer;

import healthcare.KafkaVitalEvents.VitalEvents;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, VitalEvents> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, VitalEvents> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVital(VitalEvents event) {

        kafkaTemplate.send("vital-signs", event.getPatientId(), event);

        System.out.println("Vital event sent to Kafka");
    }
}
