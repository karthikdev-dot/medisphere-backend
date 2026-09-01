package healthcare.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import healthcare.Entity.Patient;

@Repository
public interface PatientRepo
        extends MongoRepository<Patient, String> {
}