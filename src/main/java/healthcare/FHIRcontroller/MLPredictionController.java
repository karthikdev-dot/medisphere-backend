package healthcare.FHIRcontroller;


	

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;

import healthcare.DTO.MLPredictionResponse;
import healthcare.FHIRService.MLPredictionService;

	@RestController
	@RequestMapping("/api/ml")
	public class MLPredictionController {

	    @Autowired
	    private MLPredictionService mlPredictionService;

	    @GetMapping("/predict/{patientId}")
	    public MLPredictionResponse predict(
	            @PathVariable String patientId) {

	        return mlPredictionService.predictForPatient(patientId);
	    }
	}

