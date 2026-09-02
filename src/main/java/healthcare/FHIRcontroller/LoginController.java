package healthcare.FHIRcontroller;


	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;

	import healthcare.DTO.LoginRequestDTO;
	import healthcare.DTO.LoginResponseDTO;
	import healthcare.FHIRService.LoginService;

	@RestController
	@RequestMapping("/auth")
	public class LoginController {

	    @Autowired
	    private LoginService authService;


	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDTO> loginData(
	            @RequestBody LoginRequestDTO request) {

	       

	        return authService.login(request);
	    }
	}

