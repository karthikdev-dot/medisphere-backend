package healthcare.Repository;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import healthcare.Entity.Conditions;


@Repository
public interface ConditionRepo extends MongoRepository<Conditions, String>{

	  List<Conditions> findByPatientId(String patientId);
	
}
