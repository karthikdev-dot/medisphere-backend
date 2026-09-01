package healthcare.FHIRcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import healthcare.Entity.Patient;
import healthcare.FHIRService.PatientService;

@RestController
public class PatientController {

	
	@Autowired
	PatientService patientService;
	
;
    @GetMapping("/patients")
    public List<Patient> getPatients() {

        return patientService.getAllPatients();
    }
}
