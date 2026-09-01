package healthcare.FHIRcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import healthcare.Entity.Conditions;
import healthcare.FHIRService.PatientConditionService;


@RestController
@RequestMapping
public class ConditionController {

	
	@Autowired
	PatientConditionService conditionService;
	
	   @GetMapping("/conditions/{patientId}")
	    public List<Conditions> getConditionsByPatientId(
	            @PathVariable String patientId) {

	        return conditionService
	                .getPatientConditions(patientId);
	    }
	    
	    @GetMapping("/conditions")
	    public List<Conditions> getAllConditions() {

	        return conditionService
	                .getAllPatientConditions();
	    
	}
}
