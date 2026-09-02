package healthcare.FHIRService;




import java.util.Optional;
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
	    public String register(RegisterRequestDTO request) {

	    	Optional<User>data=userRepository.findByUsername(request.getUsername());
	       
	       if(data.isPresent()) {
	    	   
	    	   throw new RuntimeException("user found");
	       }
	       
	     String Hash =passwordEncoder.encode(request.getPassword());
	       
	       User user=new User();
	       
	       user.setUsername(request.getUsername());
	       user.setPassword(Hash);
	       user.setRole(request.getRole());
	  
	       userRepository.save(user);
	       
	       return "Registration successfull";
	    }
}
