package healthcare.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import healthcare.Vitalsigns.VitalSigns;



	
	@Repository
	public interface VitalRepo extends MongoRepository<VitalSigns, String> {

	    List<VitalSigns> findByPatientId(String patientId);
	}

