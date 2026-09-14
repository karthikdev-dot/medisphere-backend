package healthcare.FHIRService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import healthcare.DTO.MLPredictionRequest;
import healthcare.DTO.MLPredictionResponse;
import healthcare.Entity.Conditions;
import healthcare.Entity.Labrecords;
import healthcare.Entity.Patient;
import healthcare.Vitalsigns.VitalSigns;
import healthcare.Repository.ConditionRepo;
import healthcare.Repository.LabRecordRepo;
import healthcare.Repository.PatientRepo;
import healthcare.Repository.VitalRepo;

@Service
public class MLPredictionService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private LabRecordRepo labRecordRepo;

    @Autowired
    private VitalRepo vitalRepo;

    @Autowired
    private ConditionRepo conditionRepo;

    @Autowired
    private RestClient restClient;


    public MLPredictionResponse predictForPatient(String patientId) {

        // --------------------------------
        // 1. Get Patient
        // --------------------------------

        Patient patient = patientRepo.findByFhirId(patientId);

        if (patient == null) {
            throw new RuntimeException(
                    "Patient not found: " + patientId
            );
        }


        // --------------------------------
        // 2. Calculate Age
        // --------------------------------

        if (patient.getBirthDate() == null ||
                patient.getBirthDate().isBlank()) {

            throw new RuntimeException(
                    "Patient birth date is missing"
            );
        }

        LocalDate birthDate =
                LocalDate.parse(patient.getBirthDate());

        int age = Period.between(
                birthDate,
                LocalDate.now()
        ).getYears();


        // --------------------------------
        // 3. Get HbA1c
        // --------------------------------

        List<Labrecords> labRecords =
                labRecordRepo.findByPatientIdAndTestName(
                        patientId,
                        "HbA1c"
                );

        if (labRecords == null ||
                labRecords.isEmpty()) {

            throw new RuntimeException(
                    "HbA1c data not found for patient: "
                            + patientId
            );
        }

        Labrecords labRecord =
                labRecords.get(labRecords.size() - 1);

        if (labRecord.getValue() == null ||
                labRecord.getValue().isBlank()) {

            throw new RuntimeException(
                    "HbA1c value is missing"
            );
        }

        double hba1c =
                Double.parseDouble(labRecord.getValue());


        // --------------------------------
        // 4. Get Current Vitals
        // --------------------------------

        VitalSigns vitalSigns =
                vitalRepo.findById(patientId)
                        .orElse(null);

        if (vitalSigns == null) {

            throw new RuntimeException(
                    "Vitals not found for patient: "
                            + patientId
            );
        }

        int glucose =
                vitalSigns.getGlucose();

        int bloodPressure =
                vitalSigns.getSystolicBP();


        // --------------------------------
        // 5. Get Diabetes Condition
        // --------------------------------

        List<Conditions> conditions =
                conditionRepo.findByPatientId(patientId);

        if (conditions == null ||
                conditions.isEmpty()) {

            throw new RuntimeException(
                    "Conditions not found for patient: "
                            + patientId
            );
        }

        Conditions diabetesCondition = null;

        for (Conditions condition : conditions) {

            if (condition.getConditionName() != null &&
                    condition.getConditionName()
                            .toLowerCase()
                            .contains("diabetes")) {

                diabetesCondition = condition;
                break;
            }
        }

        if (diabetesCondition == null) {

            throw new RuntimeException(
                    "Diabetes condition not found"
            );
        }


        // --------------------------------
        // 6. Calculate Diabetes Duration
        // --------------------------------

        String onsetDate =
                diabetesCondition.getOnsetDate();

        if (onsetDate == null ||
                onsetDate.isBlank()) {

            throw new RuntimeException(
                    "Diabetes onset date is missing"
            );
        }

        LocalDate diabetesStart =
                LocalDate.parse(onsetDate);

        int diabetesDuration =
                Period.between(
                        diabetesStart,
                        LocalDate.now()
                ).getYears();


        // --------------------------------
        // 7. Create ML Request
        // --------------------------------

        MLPredictionRequest request =
                new MLPredictionRequest();

        request.setAge(age);
        request.setHba1c(hba1c);
        request.setGlucose(glucose);
        request.setDiabetesDuration(
                diabetesDuration
        );
        request.setBloodPressure(
                bloodPressure
        );


        // --------------------------------
        // 8. Send Data to Flask ML Service
        // --------------------------------

        MLPredictionResponse response =
                restClient.post()
                        .uri("/predict")
                        .body(request)
                        .retrieve()
                        .body(MLPredictionResponse.class);


        // --------------------------------
        // 9. Return Prediction
        // --------------------------------

        return response;
    }
}

