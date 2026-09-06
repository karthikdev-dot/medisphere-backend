package healthcare.KafkaConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import healthcare.KafkaVitalEvents.VitalEvents;
import healthcare.Repository.VitalRepo;
import healthcare.Vitalsigns.VitalSigns;


@Service
public class KafkaConsumer {

    @Autowired
    private VitalRepo vitalRepo;

    @KafkaListener(
    	    topics = "vital-signs",
    	    groupId = "vital-group"
    	)
    	public void consume(VitalEvents event) {

    	    System.out.println("Received vital event from Kafka");

    	    VitalSigns vitals = event.getVitals();

    	    vitals.setPatientId(event.getPatientId());

    	    vitalRepo.save(vitals);

    	    System.out.println(
    	        "Vital signs saved for patient: " + event.getPatientId()
    	    );
    }
}