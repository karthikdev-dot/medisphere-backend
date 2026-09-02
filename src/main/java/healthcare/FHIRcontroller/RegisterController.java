package healthcare.FHIRcontroller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import healthcare.DTO.RegisterRequestDTO;

import healthcare.FHIRService.RegisterService;

@RestController
@RequestMapping("/api")
public class RegisterController {

	@Autowired
	RegisterService registerService;
	
	
	@PostMapping("/reg")
	public String registration(@RequestBody RegisterRequestDTO registerReq) {
		
		return registerService.register(registerReq);
		
	}
	
}
