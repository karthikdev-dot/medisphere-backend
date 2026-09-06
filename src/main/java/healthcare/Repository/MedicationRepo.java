package healthcare.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import healthcare.Entity.PatientMedications;

public interface MedicationRepo
        extends MongoRepository<PatientMedications, String> {

    List<PatientMedications> findByPatientId(String patientId);
    
    PatientMedications findByMedicationRequestId(String medicationRequestId);

}