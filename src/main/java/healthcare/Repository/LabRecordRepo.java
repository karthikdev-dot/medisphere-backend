package healthcare.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import healthcare.Entity.Labrecords;

@Repository
public interface LabRecordRepo extends MongoRepository<Labrecords, String> {

    List<Labrecords> findByPatientId(String patientId);

    List<Labrecords> findByPatientIdAndTestName(
            String patientId,
            String testName
    );
}
