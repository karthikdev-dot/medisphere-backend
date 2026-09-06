package healthcare.FHIRService;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import healthcare.Entity.PatientMedications;
import healthcare.Repository.MedicationRepo;

@Service
public class PatientMedicationService {

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private MedicationRepo medicationRepo;


    // =====================================================
    // GET MEDICATIONS FROM FHIR
    // =====================================================
    public List<PatientMedications> getAllMedications() {

        List<PatientMedications> medications =
                new ArrayList<>();

        // Maximum medication records to import
        int maxMedications = 100;

        // First FHIR page
        Bundle bundle = fhirClient
                .search()
                .forResource(MedicationRequest.class)
                .returnBundle(Bundle.class)
                .execute();

        int pageNumber = 1;

        while (bundle != null
                && medications.size() < maxMedications) {

            System.out.println(
                    "========== MEDICATION PAGE "
                    + pageNumber
                    + " =========="
            );

            System.out.println(
                    "Medication entries = "
                    + bundle.getEntry().size()
            );


            // =====================================================
            // READ CURRENT PAGE
            // =====================================================
            for (Bundle.BundleEntryComponent entry
                    : bundle.getEntry()) {

                // Stop after 100 medication records
                if (medications.size() >= maxMedications) {
                    break;
                }


                MedicationRequest request =
                        (MedicationRequest)
                                entry.getResource();


                // Convert FHIR → MongoDB
                PatientMedications medication =
                        convertToMedication(request);


                // Make sure MedicationRequest has a patient
                if (medication.getPatientId() == null
                        || medication.getPatientId().isEmpty()) {

                    System.out.println(
                            "Skipping medication without patient"
                    );

                    continue;
                }


                // =====================================================
                // CHECK DUPLICATE
                // =====================================================

                // If your MedicationRepo has this method,
                // use it to prevent duplicate MedicationRequests.
                PatientMedications existing =
                        medicationRepo
                                .findByMedicationRequestId(
                                        medication
                                                .getMedicationRequestId()
                                );


                if (existing == null) {

                    medicationRepo.save(medication);

                    medications.add(medication);

                    System.out.println(
                            "Saved MedicationRequest = "
                            + medication
                                    .getMedicationRequestId()
                    );

                    System.out.println(
                            "Patient FHIR ID = "
                            + medication.getPatientId()
                    );

                    System.out.println(
                            "Medication = "
                            + medication.getMedicationName()
                    );

                } else {

                    System.out.println(
                            "Already exists = "
                            + medication
                                    .getMedicationRequestId()
                    );
                }
            }


            // =====================================================
            // STOP IF 100 MEDICATIONS
            // =====================================================
            if (medications.size() >= maxMedications) {
                break;
            }


            // =====================================================
            // LOAD NEXT FHIR PAGE
            // =====================================================
            if (bundle.getLink(Bundle.LINK_NEXT) != null) {

                String nextUrl =
                        bundle
                                .getLink(Bundle.LINK_NEXT)
                                .getUrl();

                System.out.println(
                        "Loading next medication page..."
                );

                bundle = fhirClient
                        .loadPage()
                        .byUrl(nextUrl)
                        .andReturnBundle(Bundle.class)
                        .execute();

                pageNumber++;

            } else {

                bundle = null;

                System.out.println(
                        "========== NO MORE MEDICATION PAGES =========="
                );
            }
        }


        System.out.println(
                "======================================"
        );

        System.out.println(
                "MEDICATIONS SAVED = "
                + medications.size()
        );

        System.out.println(
                "======================================"
        );

        return medications;
    }


    // =====================================================
    // GET MEDICATIONS FOR ONE PATIENT
    // =====================================================
    public List<PatientMedications> getMedicationsByPatientId(
            String patientId) {

        List<PatientMedications> medications =
                new ArrayList<>();

        Bundle bundle = fhirClient
                .search()
                .forResource(MedicationRequest.class)
                .where(
                        MedicationRequest.SUBJECT.hasId(
                                patientId
                        )
                )
                .returnBundle(Bundle.class)
                .execute();


        System.out.println(
                "Medication entries for patient = "
                + bundle.getEntry().size()
        );


        for (Bundle.BundleEntryComponent entry
                : bundle.getEntry()) {

            MedicationRequest request =
                    (MedicationRequest)
                            entry.getResource();


            PatientMedications medication =
                    convertToMedication(request);


            medicationRepo.save(medication);

            medications.add(medication);
        }


        return medications;
    }


    // =====================================================
    // GET ONE MEDICATION REQUEST
    // =====================================================
    public PatientMedications getMedicationById(
            String id) {

        MedicationRequest request =
                fhirClient
                        .read()
                        .resource(MedicationRequest.class)
                        .withId(id)
                        .execute();


        PatientMedications medication =
                convertToMedication(request);


        medicationRepo.save(medication);

        return medication;
    }


    // =====================================================
    // CONVERT FHIR MEDICATIONREQUEST → MONGODB
    // =====================================================
    private PatientMedications convertToMedication(
            MedicationRequest request) {

        PatientMedications medication =
                new PatientMedications();


        // =====================================================
        // MedicationRequest ID
        // =====================================================
        medication.setMedicationRequestId(
                request
                        .getIdElement()
                        .getIdPart()
        );


        // =====================================================
        // PATIENT FHIR ID
        // =====================================================
        if (request.hasSubject()
                && request.getSubject().hasReference()) {

            String reference =
                    request.getSubject()
                            .getReference();


            if (reference.startsWith("Patient/")) {

                reference =
                        reference.substring(
                                "Patient/".length()
                        );
            }


            medication.setPatientId(reference);
        }


        // =====================================================
        // STATUS
        // =====================================================
        if (request.hasStatus()) {

            medication.setStatus(
                    request
                            .getStatus()
                            .toCode()
            );
        }


        // =====================================================
        // MEDICATION NAME
        // =====================================================
        if (request.hasMedicationCodeableConcept()) {

            if (request
                    .getMedicationCodeableConcept()
                    .hasText()) {

                medication.setMedicationName(
                        request
                                .getMedicationCodeableConcept()
                                .getText()
                );

            } else if (request
                    .getMedicationCodeableConcept()
                    .hasCoding()) {

                medication.setMedicationName(
                        request
                                .getMedicationCodeableConcept()
                                .getCodingFirstRep()
                                .getDisplay()
                );
            }

        } else if (request.hasMedicationReference()) {

            medication.setMedicationName(
                    request
                            .getMedicationReference()
                            .getDisplay()
            );
        }


        // =====================================================
        // DOSAGE
        // =====================================================
        if (request.hasDosageInstruction()) {

            var dosage =
                    request
                            .getDosageInstructionFirstRep();


            if (dosage.hasText()) {

                medication.setDosage(
                        dosage.getText()
                );
            }


            // =================================================
            // FREQUENCY
            // =================================================
            if (dosage.hasTiming()
                    && dosage
                            .getTiming()
                            .hasRepeat()) {

                var repeat =
                        dosage
                                .getTiming()
                                .getRepeat();

                String frequency = "";


                if (repeat.hasFrequency()) {

                    frequency =
                            String.valueOf(
                                    repeat.getFrequency()
                            );
                }


                if (repeat.hasPeriod()) {

                    frequency +=
                            " every "
                            + repeat.getPeriod();
                }


                if (repeat.hasPeriodUnit()) {

                    frequency +=
                            " "
                            + repeat
                                    .getPeriodUnit()
                                    .toCode();
                }


                medication.setFrequency(
                        frequency
                );
            }


            // =================================================
            // ROUTE
            // =================================================
            if (dosage.hasRoute()) {

                medication.setRoute(
                        dosage
                                .getRoute()
                                .getText()
                );
            }
        }


        // =====================================================
        // AUTHORED DATE
        // =====================================================
        if (request.hasAuthoredOn()) {

            medication.setAuthoredOn(
                    request
                            .getAuthoredOnElement()
                            .getValueAsString()
            );
        }


        return medication;
    }
}