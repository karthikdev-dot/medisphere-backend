package healthcare.Repository;




import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import healthcare.Vitalsigns.VitalSigns;



	
	
	@Repository
	public interface VitalRepo extends MongoRepository<VitalSigns, String> {
	}

