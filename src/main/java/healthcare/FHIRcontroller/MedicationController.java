package healthcare.FHIRcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import healthcare.Entity.PatientMedications;
import healthcare.FHIRService.PatientMedicationService;


@RestController
@RequestMapping("/api")
public class MedicationController {

	
	
	@Autowired
	PatientMedicationService medicationService;
	
	   @GetMapping("/medications")
	    public List<PatientMedications> getAllMedications() {

	        return medicationService.getAllMedications();
	    }


	    // Get medications of one patient
	    @GetMapping("/patients/{patientId}/medications")
	    public List<PatientMedications> getPatientMedications(
	            @PathVariable String patientId) {

	        return medicationService
	                .getMedicationsByPatientId(patientId);
	    }


	    // Get one MedicationRequest by ID
	    @GetMapping("/medications/{id}")
	    public PatientMedications getMedicationById(
	            @PathVariable String id) {

	        return medicationService
	                .getMedicationById(id);
	    }
}
