package healthcare.Repository;

	import org.springframework.data.mongodb.repository.MongoRepository;

	import healthcare.Entity.AuditLog;

	public interface AuditLogRepo extends MongoRepository<AuditLog, String> {

	}

