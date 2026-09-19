package healthcare.KafkaConsumer;

import healthcare.AnomolyDetectionService.AnomolyDetectionService;
import healthcare.KafkaVitalEvents.VitalEvents;
import healthcare.Repository.VitalRepo;
import healthcare.VitalValidationService.VitalValidation;
import healthcare.Vitalsigns.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private VitalRepo vitalRepo;

    // Milestone 3 - Anomaly Detection
    @Autowired
    private AnomolyDetectionService anomalyDetectionService;

    // Milestone 3 - Vitals Validation
    @Autowired
    private VitalValidation vitalValidationService;


    @KafkaListener(
            topics = "vital-signs",
            groupId = "vital-group-test"
    )
    public void consume(VitalEvents event) {

        System.out.println("=================================");
        System.out.println("Received Vital Event from Kafka");

        System.out.println(
                "Patient ID: " + event.getPatientId()
        );

        // Get vitals from event
        VitalSigns vitals = event.getVitals();

        // Check null
        if (vitals == null) {

            System.out.println(
                    "Invalid event: Vitals are null"
            );

            return;
        }

        // Set patient ID
        vitals.setPatientId(
                event.getPatientId()
        );


        // =================================
        // MILESTONE 3 - VITALS VALIDATION
        // =================================

        boolean valid =
                vitalValidationService.isValid(vitals);

        if (!valid) {

            System.out.println(
                    "INVALID VITALS - Event rejected"
            );

            System.out.println(
                    "Patient ID: "
                            + event.getPatientId()
            );

            return;
        }

        System.out.println(
                "Vitals validation successful"
        );


        // =================================
        // SAVE TO MONGODB
        // =================================

        vitalRepo.save(vitals);

        System.out.println(
                "Vitals saved for patient: "
                        + event.getPatientId()
        );


        // =================================
        // ANOMALY DETECTION
        // =================================

        anomalyDetectionService.checkVitals(vitals);


        System.out.println("=================================");
    }
}