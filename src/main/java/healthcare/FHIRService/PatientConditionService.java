package healthcare.FHIRService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;

import healthcare.Entity.Conditions;
import healthcare.Repository.ConditionRepo;

@Service
public class PatientConditionService {

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private ConditionRepo conditionRepo;


    // =========================================================
    // FETCH CONDITIONS FROM FHIR AND SAVE TO MONGODB
    // =========================================================

    public String fetchAndSaveConditionsByPatientId(String patientId) {

        Bundle bundle = fhirClient
                .search()
                .forResource(Condition.class)
                .where(
                        Condition.SUBJECT.hasId(patientId)
                )
                .returnBundle(Bundle.class)
                .execute();


        // Check Bundle
        if (bundle == null) {
            return "FHIR Bundle is null";
        }


        // Check entries
        if (!bundle.hasEntry()) {
            return "No conditions found in FHIR for patient: "
                    + patientId;
        }


        System.out.println(
                "Conditions found in FHIR = "
                + bundle.getEntry().size()
        );


        List<Conditions> conditionList =
                new ArrayList<>();


        // =====================================================
        // LOOP THROUGH FHIR CONDITIONS
        // =====================================================

        for (Bundle.BundleEntryComponent entry
                : bundle.getEntry()) {

            if (!entry.hasResource()) {
                continue;
            }


            if (!(entry.getResource() instanceof Condition)) {
                continue;
            }


            Condition fhirCondition =
                    (Condition) entry.getResource();


            Conditions condition =
                    new Conditions();


            // -------------------------------------------------
            // CONDITION ID
            // -------------------------------------------------

            condition.setConditionId(
                    fhirCondition
                            .getIdElement()
                            .getIdPart()
            );


            // -------------------------------------------------
            // PATIENT ID
            // -------------------------------------------------

            if (fhirCondition.hasSubject()) {

                String reference =
                        fhirCondition
                                .getSubject()
                                .getReference();


                if (reference != null
                        && reference.startsWith("Patient/")) {

                    condition.setPatientId(
                            reference.substring(
                                    "Patient/".length()
                            )
                    );
                }
            }


            // -------------------------------------------------
            // CONDITION NAME + CODE
            // -------------------------------------------------

            if (fhirCondition.hasCode()) {

                // Text
                if (fhirCondition
                        .getCode()
                        .hasText()) {

                    condition.setConditionName(
                            fhirCondition
                                    .getCode()
                                    .getText()
                    );
                }


                // Coding
                if (fhirCondition
                        .getCode()
                        .hasCoding()) {

                    var coding =
                            fhirCondition
                                    .getCode()
                                    .getCodingFirstRep();


                    condition.setCodeSystem(
                            coding.getSystem()
                    );


                    condition.setConditionCode(
                            coding.getCode()
                    );


                    // If text is empty,
                    // use display
                    if (condition.getConditionName() == null
                            || condition.getConditionName().isEmpty()) {

                        condition.setConditionName(
                                coding.getDisplay()
                        );
                    }
                }
            }


            // -------------------------------------------------
            // CLINICAL STATUS
            // -------------------------------------------------

            if (fhirCondition.hasClinicalStatus()
                    && fhirCondition
                            .getClinicalStatus()
                            .hasCoding()) {

                condition.setClinicalStatus(
                        fhirCondition
                                .getClinicalStatus()
                                .getCodingFirstRep()
                                .getCode()
                );
            }


            // -------------------------------------------------
            // VERIFICATION STATUS
            // -------------------------------------------------

            if (fhirCondition.hasVerificationStatus()
                    && fhirCondition
                            .getVerificationStatus()
                            .hasCoding()) {

                condition.setVerificationStatus(
                        fhirCondition
                                .getVerificationStatus()
                                .getCodingFirstRep()
                                .getCode()
                );
            }


            // -------------------------------------------------
            // ONSET DATE
            // -------------------------------------------------

            if (fhirCondition.hasOnsetDateTimeType()) {

                condition.setOnsetDate(
                        fhirCondition
                                .getOnsetDateTimeType()
                                .getValueAsString()
                );
            }


            // -------------------------------------------------
            // RECORDED DATE
            // -------------------------------------------------

            if (fhirCondition.hasRecordedDate()) {

                condition.setRecordedDate(
                        fhirCondition
                                .getRecordedDateElement()
                                .getValueAsString()
                );
            }


            // Add to list
            conditionList.add(condition);
        }


        // =====================================================
        // SAVE ALL CONDITIONS TO MONGODB
        // =====================================================

        conditionRepo.saveAll(conditionList);


        return conditionList.size()
                + " conditions saved successfully";
    }


    // =========================================================
    // GET ALL CONDITIONS FROM MONGODB
    // =========================================================

    public List<Conditions> getAllPatientConditions() {

        return conditionRepo.findAll();
    }


    // =========================================================
    // GET CONDITIONS BY PATIENT ID
    // =========================================================

    public List<Conditions> getPatientConditions(
            String patientId) {

        return conditionRepo
                .findByPatientId(patientId);
    }


    // =========================================================
    // GET ONE CONDITION BY CONDITION ID
    // =========================================================

    public Conditions getConditionById(
            String conditionId) {

        return conditionRepo
                .findById(conditionId)
                .orElse(null);
    }
}