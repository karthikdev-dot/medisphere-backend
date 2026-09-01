package healthcare.FHIRService;

import java.util.ArrayList;
import java.util.List;

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
    // FETCH LAB RECORDS FROM FHIR AND SAVE TO MONGODB
    // =====================================================

    public List<Labrecords> fetchAndSaveLabRecords() {

        Bundle bundle = fhirClient
                .search()
                .forResource(Observation.class)
                .count(50)
                .returnBundle(Bundle.class)
                .execute();


        List<Labrecords> labRecords = new ArrayList<>();


        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {

            Observation observation =
                    (Observation) entry.getResource();


            // =====================================================
            // TEST NAME
            // =====================================================

            String testName = null;


            if (observation.hasCode()) {

                if (observation.getCode().hasText()) {

                    testName =
                            observation.getCode().getText();

                } else if (!observation.getCode()
                        .getCoding()
                        .isEmpty()) {

                    testName =
                            observation.getCode()
                                    .getCodingFirstRep()
                                    .getDisplay();
                }
            }


            if (testName == null) {
                continue;
            }


            // =====================================================
            // FILTER
            // ONLY HEMOGLOBIN, GLUCOSE, CHOLESTEROL
            // =====================================================

            String test =
                    testName.toLowerCase();


            if (!(test.contains("hemoglobin")
                    || test.contains("glucose")
                    || test.contains("cholesterol"))) {

                continue;
            }


            // =====================================================
            // CREATE LAB RECORD
            // =====================================================

            Labrecords lab =
                    new Labrecords();


            // =====================================================
            // FHIR OBSERVATION ID
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

            if (observation.hasSubject()) {

                String patientReference =
                        observation
                                .getSubject()
                                .getReference();


                if (patientReference != null
                        && patientReference.contains("/")) {

                    String patientId =
                            patientReference.substring(
                                    patientReference.lastIndexOf("/") + 1
                            );


                    lab.setPatientId(patientId);
                }
            }


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
                        observation.getValueQuantity();


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

                Observation.ObservationReferenceRangeComponent range =
                        observation
                                .getReferenceRangeFirstRep();


                String referenceRange = "";


                if (range.hasLow()
                        && range.getLow().hasValue()) {

                    referenceRange =
                            range.getLow()
                                    .getValue()
                                    .toPlainString();
                }


                if (range.hasHigh()
                        && range.getHigh().hasValue()) {

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


            // Add to list
            labRecords.add(lab);
        }


        // =====================================================
        // SAVE TO MONGODB
        // =====================================================

        if (!labRecords.isEmpty()) {

            labRecordRepository.saveAll(
                    labRecords
            );

            System.out.println(
                    "Saved lab records = "
                            + labRecords.size()
            );

        } else {

            System.out.println(
                    "No Hemoglobin, Glucose or Cholesterol records found"
            );
        }


        // Return data to controller
        return labRecords;
    }


    // =====================================================
    // GET ALL LAB RECORDS FROM MONGODB
    // =====================================================

    public List<Labrecords> getAllLabRecords() {

        return labRecordRepository.findAll();
    }


    // =====================================================
    // GET LAB RECORD BY MONGODB ID
    // =====================================================

    public Labrecords getLabRecordById(String id) {

        return labRecordRepository
                .findById(id)
                .orElse(null);
    }
}