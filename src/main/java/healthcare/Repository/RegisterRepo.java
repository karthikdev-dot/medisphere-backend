package healthcare.Repository;

	import java.util.Optional;

	import org.springframework.data.mongodb.repository.MongoRepository;

	import healthcare.Entity.User;

	public interface RegisterRepo
	        extends MongoRepository<User, String> {

	    Optional<User> findByUsername(String username);
	    
	}

