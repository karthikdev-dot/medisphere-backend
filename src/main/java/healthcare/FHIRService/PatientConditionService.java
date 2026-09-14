
package healthcare.FHIRService;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthcare.Entity.Conditions;
import healthcare.Repository.ConditionRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientConditionService {

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private ConditionRepo conditionRepo;


    // =========================================================
    // FETCH CONDITIONS FROM FHIR AND SAVE TO MONGODB
    // Maximum 4 pages
    // =========================================================

    public String fetchAndSaveConditionsByPatientId(String patientId) {

        List<Conditions> conditionList = new ArrayList<>();

        int maxPages = 4;
        int currentPage = 1;

        // =====================================================
        // FIRST PAGE
        // =====================================================

        Bundle bundle = fhirClient
                .search()
                .forResource(Condition.class)
                .where(
                        Condition.SUBJECT.hasId(patientId)
                )
                .count(100)
                .returnBundle(Bundle.class)
                .execute();


        // =====================================================
        // FETCH UP TO 4 PAGES
        // =====================================================

        while (bundle != null && currentPage <= maxPages) {

            System.out.println(
                    "Fetching Condition page: "
                            + currentPage
            );


            // =================================================
            // CHECK ENTRIES
            // =================================================

            if (bundle.hasEntry()) {

                System.out.println(
                        "Conditions found on page "
                                + currentPage
                                + " = "
                                + bundle.getEntry().size()
                );


                // =============================================
                // LOOP THROUGH CONDITIONS
                // =============================================

                for (Bundle.BundleEntryComponent entry
                        : bundle.getEntry()) {

                    if (!entry.hasResource()) {
                        continue;
                    }


                    if (!(entry.getResource()
                            instanceof Condition)) {

                        continue;
                    }


                    Condition fhirCondition =
                            (Condition) entry.getResource();


                    Conditions condition =
                            new Conditions();


                    // =========================================
                    // CONDITION ID
                    // =========================================

                    condition.setConditionId(
                            fhirCondition
                                    .getIdElement()
                                    .getIdPart()
                    );


                    // =========================================
                    // PATIENT ID
                    // =========================================

                    if (fhirCondition.hasSubject()) {

                        String reference =
                                fhirCondition
                                        .getSubject()
                                        .getReference();


                        if (reference != null
                                && reference.startsWith(
                                "Patient/")) {

                            condition.setPatientId(
                                    reference.substring(
                                            "Patient/".length()
                                    )
                            );
                        }
                    }


                    // =========================================
                    // CONDITION NAME + CODE
                    // =========================================

                    if (fhirCondition.hasCode()) {


                        // -------------------------------------
                        // TEXT
                        // -------------------------------------

                        if (fhirCondition
                                .getCode()
                                .hasText()) {

                            condition.setConditionName(
                                    fhirCondition
                                            .getCode()
                                            .getText()
                            );
                        }


                        // -------------------------------------
                        // CODING
                        // -------------------------------------

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
                            // use coding display

                            if (condition
                                    .getConditionName() == null
                                    || condition
                                    .getConditionName()
                                    .isEmpty()) {

                                condition.setConditionName(
                                        coding.getDisplay()
                                );
                            }
                        }
                    }


                    // =========================================
                    // CLINICAL STATUS
                    // =========================================

                    if (fhirCondition
                            .hasClinicalStatus()
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


                    // =========================================
                    // VERIFICATION STATUS
                    // =========================================

                    if (fhirCondition
                            .hasVerificationStatus()
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


                    // =========================================
                    // ONSET DATE
                    // =========================================

                    if (fhirCondition
                            .hasOnsetDateTimeType()) {

                        condition.setOnsetDate(
                                fhirCondition
                                        .getOnsetDateTimeType()
                                        .getValueAsString()
                        );
                    }


                    // =========================================
                    // RECORDED DATE
                    // =========================================

                    if (fhirCondition
                            .hasRecordedDate()) {

                        condition.setRecordedDate(
                                fhirCondition
                                        .getRecordedDateElement()
                                        .getValueAsString()
                        );
                    }


                    // =========================================
                    // ADD TO LIST
                    // =========================================

                    conditionList.add(condition);
                }
            }


            // =================================================
            // FIND NEXT PAGE
            // =================================================

            String nextUrl = null;

            for (Bundle.BundleLinkComponent link
                    : bundle.getLink()) {

                if ("next".equals(link.getRelation())) {

                    nextUrl = link.getUrl();

                    break;
                }
            }

            // =================================================
            // NO NEXT PAGE
            // =================================================

            if (nextUrl == null
                    || nextUrl.isEmpty()) {

                break;
            }


            // =================================================
            // FETCH NEXT PAGE
            // =================================================

            bundle = fhirClient
                    .fetchResourceFromUrl(
                            Bundle.class,
                            nextUrl
                    );


            currentPage++;
        }


        // =====================================================
        // SAVE CONDITIONS TO MONGODB
        // =====================================================

        if (conditionList.isEmpty()) {

            return "No conditions found in FHIR for patient: "
                    + patientId;
        }


        conditionRepo.saveAll(conditionList);


        // =====================================================
        // FINAL RESULT
        // =====================================================

        return conditionList.size()
                + " conditions saved successfully from "
                + currentPage
                + " page(s)";
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

