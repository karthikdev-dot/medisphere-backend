package healthcare.FHIRService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthcare.Entity.Patient;
import healthcare.KafkaProducer.KafkaProducer;
import healthcare.KafkaVitalEvents.VitalEvents;
import healthcare.Repository.PatientRepo;
import healthcare.Vitalsigns.VitalSigns;


@Service
public class VitalService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private Vitalgeneretor generatorService;

    @Autowired
    private KafkaProducer kafkaProducer;

    public String generateAndSend(String patientId) {

        System.out.println("Searching patient: " + patientId);

        Patient patient = patientRepo.findByFhirId(patientId);

        if (patient == null) {
            System.out.println("PATIENT NOT FOUND");
            return "Patient not found";
        }

        System.out.println("PATIENT FOUND: " + patient.getName());

        VitalSigns vitals = generatorService.generateVitals();

        // Make sure patient ID is stored in VitalSigns
        vitals.setPatientId(patientId);

        VitalEvents event = new VitalEvents(patientId, vitals);

        kafkaProducer.sendVital(event);

        return "Vital signs sent to Kafka";
    }
}