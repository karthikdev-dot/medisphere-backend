package healthcare.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import healthcare.Entity.Labrecords;

public interface LabRecordRepo extends MongoRepository<Labrecords, String> {
	}
