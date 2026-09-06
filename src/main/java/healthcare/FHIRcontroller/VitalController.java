package healthcare.FHIRcontroller;



	



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import healthcare.FHIRService.VitalService;


	@RestController
	@RequestMapping("/api/vitals")
	public class VitalController {
		

		@Autowired
	    private  VitalService vitalService;

		

	    @GetMapping("/{patientId}")
	    public String generateVitals(
	            @PathVariable String patientId) {

	        return vitalService.generateAndSend(patientId);
	    }
	}

