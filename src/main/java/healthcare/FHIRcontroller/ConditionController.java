package healthcare.FHIRcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import healthcare.Entity.Conditions;
import healthcare.FHIRService.PatientConditionService;

@RestController
@RequestMapping("/api")
public class ConditionController {

    @Autowired
    private PatientConditionService conditionService;

    // Fetch conditions from FHIR and save into MongoDB
    @PostMapping("/conditions/save/{patientId}")
    public String saveConditions(
            @PathVariable String patientId) {

        return conditionService
                .fetchAndSaveConditionsByPatientId(patientId);
    }

    // Get all conditions from MongoDB
    @GetMapping("/conditions")
    public List<Conditions> getAllConditions() {

        return conditionService
                .getAllPatientConditions();
    }

    // Get conditions for one patient from MongoDB
    @GetMapping("/conditions/{patientId}")
    public List<Conditions> getConditionsByPatientId(
            @PathVariable String patientId) {

        return conditionService
                .getPatientConditions(patientId);
    }

    // Get one condition from MongoDB
    @GetMapping("/condition/{conditionId}")
    public Conditions getConditionById(
            @PathVariable String conditionId) {

        return conditionService
                .getConditionById(conditionId);
    }
}
