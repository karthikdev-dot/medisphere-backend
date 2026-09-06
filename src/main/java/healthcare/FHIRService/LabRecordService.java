package healthcare.FHIRService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import healthcare.Entity.Labrecords;
import healthcare.Repository.LabRecordRepo;

@Service
public class LabRecordService {

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private LabRecordRepo labRecordRepository;


    // =====================================================
    // FETCH LAB RECORDS FROM FHIR
    // CHECK MAXIMUM 3 PAGES
    // STOP AFTER 100 UNIQUE PATIENTS
    // =====================================================

    public List<Labrecords> fetchAndSaveLabRecords() {

        List<Labrecords> labRecords = new ArrayList<>();

        // Store UNIQUE patient IDs
        Set<String> patientIds = new HashSet<>();

        // =====================================================
        // FIRST PAGE
        // =====================================================

        Bundle bundle = fhirClient
                .search()
                .forResource(Observation.class)
                .count(100)
                .returnBundle(Bundle.class)
                .execute();

        // Maximum 3 pages
        int pageCount = 0;

        // =====================================================
        // PROCESS PAGES
        // =====================================================

        while (bundle != null
                && patientIds.size() < 100
                && pageCount < 3) {

            pageCount++;

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Checking FHIR Page = " + pageCount
            );

            System.out.println(
                    "Observation records = "
                            + bundle.getEntry().size()
            );

            System.out.println(
                    "===================================="
            );


            // =====================================================
            // PROCESS OBSERVATIONS
            // =====================================================

            for (Bundle.BundleEntryComponent entry
                    : bundle.getEntry()) {

                if (!(entry.getResource()
                        instanceof Observation)) {

                    continue;
                }

                Observation observation =
                        (Observation) entry.getResource();


                // =====================================================
                // PATIENT ID
                // =====================================================

                if (!observation.hasSubject()
                        || !observation
                                .getSubject()
                                .hasReference()) {

                    continue;
                }

                String patientReference =
                        observation
                                .getSubject()
                                .getReference();

                if (patientReference == null
                        || patientReference.isEmpty()) {

                    continue;
                }

                String patientId =
                        patientReference.substring(
                                patientReference
                                        .lastIndexOf("/") + 1
                        );

                if (patientId.isEmpty()) {
                    continue;
                }


                // =====================================================
                // TEST NAME
                // =====================================================

                String testName = null;

                if (observation.hasCode()) {

                    if (observation
                            .getCode()
                            .hasText()) {

                        testName =
                                observation
                                        .getCode()
                                        .getText();

                    } else if (!observation
                            .getCode()
                            .getCoding()
                            .isEmpty()) {

                        testName =
                                observation
                                        .getCode()
                                        .getCodingFirstRep()
                                        .getDisplay();
                    }
                }


                if (testName == null
                        || testName.isBlank()) {

                    continue;
                }


                // =====================================================
                // FILTER
                //
                // CHOLESTEROL
                // HEMOGLOBIN
                // GLUCOSE
                // HBA1C
                // DIABETES
                // =====================================================

                String test =
                        testName.toLowerCase();

                boolean isRequiredTest =
                        test.contains("cholesterol")
                        || test.contains("hemoglobin")
                        || test.contains("haemoglobin")
                        || test.contains("glucose")
                        || test.contains("hba1c")
                        || test.contains("a1c")
                        || test.contains("diabetes");


                if (!isRequiredTest) {
                    continue;
                }


                // =====================================================
                // ONLY 100 UNIQUE PATIENTS
                // =====================================================

                if (!patientIds.contains(patientId)
                        && patientIds.size() >= 100) {

                    break;
                }

                patientIds.add(patientId);


                // =====================================================
                // CREATE LAB RECORD
                // =====================================================

                Labrecords lab =
                        new Labrecords();


                // =====================================================
                // OBSERVATION ID
                // =====================================================

                if (observation.hasIdElement()) {

                    lab.setObservationId(
                            observation
                                    .getIdElement()
                                    .getIdPart()
                    );
                }


                // =====================================================
                // PATIENT ID
                // =====================================================

                lab.setPatientId(patientId);


                // =====================================================
                // TEST NAME
                // =====================================================

                lab.setTestName(testName);


                // =====================================================
                // STATUS
                // =====================================================

                if (observation.hasStatus()) {

                    lab.setStatus(
                            observation
                                    .getStatus()
                                    .toCode()
                    );
                }


                // =====================================================
                // VALUE AND UNIT
                // =====================================================

                if (observation.hasValueQuantity()) {

                    Quantity quantity =
                            observation
                                    .getValueQuantity();

                    if (quantity.hasValue()) {

                        lab.setValue(
                                quantity
                                        .getValue()
                                        .toPlainString()
                        );
                    }

                    if (quantity.hasUnit()) {

                        lab.setUnit(
                                quantity.getUnit()
                        );
                    }
                }


                // =====================================================
                // REFERENCE RANGE
                // =====================================================

                if (observation.hasReferenceRange()) {

                    Observation
                            .ObservationReferenceRangeComponent range =
                            observation
                                    .getReferenceRangeFirstRep();

                    String referenceRange = "";


                    if (range.hasLow()
                            && range.getLow()
                                    .hasValue()) {

                        referenceRange =
                                range.getLow()
                                        .getValue()
                                        .toPlainString();
                    }


                    if (range.hasHigh()
                            && range.getHigh()
                                    .hasValue()) {

                        if (!referenceRange.isEmpty()) {

                            referenceRange += " - ";
                        }

                        referenceRange +=
                                range.getHigh()
                                        .getValue()
                                        .toPlainString();
                    }


                    lab.setReferenceRange(
                            referenceRange
                    );
                }


                // =====================================================
                // EFFECTIVE DATE
                // =====================================================

                if (observation
                        .hasEffectiveDateTimeType()) {

                    lab.setEffectiveDate(
                            observation
                                    .getEffectiveDateTimeType()
                                    .getValueAsString()
                    );
                }


                // =====================================================
                // ADD RECORD
                // =====================================================

                labRecords.add(lab);


                System.out.println(
                        "Patient = "
                                + patientId
                                + " | Test = "
                                + testName
                );
            }


            // =====================================================
            // STOP IF 100 PATIENTS FOUND
            // =====================================================

            if (patientIds.size() >= 100) {

                System.out.println(
                        "100 unique patients found."
                );

                break;
            }


            // =====================================================
            // GO TO NEXT PAGE
            // MAXIMUM 3 PAGES
            // =====================================================

            if (pageCount < 3
                    && bundle.getLink(
                            Bundle.LINK_NEXT) != null) {

                String nextUrl =
                        bundle
                                .getLink(
                                        Bundle.LINK_NEXT)
                                .getUrl();

                bundle =
                        fhirClient
                                .loadPage()
                                .byUrl(nextUrl)
                                .andReturnBundle(
                                        Bundle.class)
                                .execute();

            } else {

                System.out.println(
                        "Reached maximum of 3 pages."
                );

                break;
            }
        }


        // =====================================================
        // SAVE TO MONGODB
        // =====================================================

        if (!labRecords.isEmpty()) {

            labRecordRepository.saveAll(
                    labRecords
            );

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Pages checked = "
                            + pageCount
            );

            System.out.println(
                    "Unique patients = "
                            + patientIds.size()
            );

            System.out.println(
                    "Lab records saved = "
                            + labRecords.size()
            );

            System.out.println(
                    "===================================="
            );

        } else {

            System.out.println(
                    "No cholesterol, hemoglobin "
                    + "or diabetes-related records found."
            );
        }


        return labRecords;
    }
}