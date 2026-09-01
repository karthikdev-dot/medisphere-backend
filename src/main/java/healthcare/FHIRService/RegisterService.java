package healthcare.FHIRService;



	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.stereotype.Service;

	import healthcare.DTO.RegisterRequestDTO;
	import healthcare.Entity.User;
	import healthcare.Repository.RegisterRepo;
  

	@Service
	public class RegisterService {

	    @Autowired
	    private RegisterRepo userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	  


	    // REGISTER
	    public User register(RegisterRequestDTO request) {

	        if (userRepository.existsByUsername(
	                request.getUsername())) {

	            throw new RuntimeException(
	                    "Username already exists");
	        }

	        User user = new User();

	        user.setUsername(
	                request.getUsername()
	        );

	        user.setPassword(
	                passwordEncoder.encode(
	                        request.getPassword()
	                )
	        );

	        user.setRole(
	                request.getRole()
	        );

	        return userRepository.save(user);
	    }
}
