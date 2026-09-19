package healthcare.AlertRepo;

import healthcare.Alert.AlertEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepo extends MongoRepository<AlertEntity, String> {

	List<AlertEntity> findByPatientId(String patientId);

	List<AlertEntity> findByStatus(String status);

	List<AlertEntity> findByPatientIdAndStatus(
	        String patientId,
	        String status
	);

	Optional<AlertEntity> findByPatientIdAndAlertTypeAndStatus(
	        String patientId,
	        String alertType,
	        String status
	);
}