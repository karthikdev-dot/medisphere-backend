package healthcare.Repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import healthcare.Entity.NotificationEntity;

import java.util.List;

public interface NotificationRepo
        extends MongoRepository<NotificationEntity, String> {

    List<NotificationEntity> findByPatientId(String patientId);

    List<NotificationEntity> findByRecipient(String recipient);

    List<NotificationEntity> findByStatus(String status);
}
