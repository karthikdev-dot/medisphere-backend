
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


    // GET ALL MEDICATION REQUESTS
    public List<PatientMedications> getAllMedications() {

        List<PatientMedications> medications = new ArrayList<>();

        Bundle bundle = fhirClient
                .search()
                .forResource(MedicationRequest.class)
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Medication entries = " + bundle.getEntry().size());

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {

            MedicationRequest request =
                    (MedicationRequest) entry.getResource();

            PatientMedications medication =
                    convertToMedication(request);

            medicationRepo.save(medication);

            medications.add(medication);

            System.out.println(
                    "MedicationRequest ID = "
                    + medication.getMedicationRequestId()
            );
        }

        return medications;
    }


    // GET MEDICATIONS FOR ONE PATIENT
    public List<PatientMedications> getMedicationsByPatientId(
            String patientId) {

        List<PatientMedications> medications = new ArrayList<>();

        Bundle bundle = fhirClient
                .search()
                .forResource(MedicationRequest.class)
                .where(
                    MedicationRequest.SUBJECT.hasId(patientId)
                )
                .returnBundle(Bundle.class)
                .execute();

        System.out.println(
                "Medication entries for patient = "
                + bundle.getEntry().size()
        );

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {

            MedicationRequest request =
                    (MedicationRequest) entry.getResource();

            PatientMedications medication =
                    convertToMedication(request);

            medicationRepo.save(medication);

            medications.add(medication);
        }

        return medications;
    }


    // GET ONE MEDICATION REQUEST BY ID
    public PatientMedications getMedicationById(String id) {

        MedicationRequest request = fhirClient
                .read()
                .resource(MedicationRequest.class)
                .withId(id)
                .execute();

        PatientMedications medication =
                convertToMedication(request);

        medicationRepo.save(medication);

        return medication;
    }


    // CONVERT FHIR MedicationRequest → MongoDB MODEL
    private PatientMedications convertToMedication(
            MedicationRequest request) {

        PatientMedications medication =
                new PatientMedications();


        // MedicationRequest ID
        medication.setMedicationRequestId(
                request.getIdElement().getIdPart()
        );


        // Patient ID
        if (request.hasSubject()
                && request.getSubject().hasReference()) {

            String reference =
                    request.getSubject().getReference();

            if (reference.startsWith("Patient/")) {

                reference =
                        reference.substring("Patient/".length());
            }

            medication.setPatientId(reference);
        }


        // Status
        if (request.hasStatus()) {

            medication.setStatus(
                    request.getStatus().toCode()
            );
        }


        // Medication name
        if (request.hasMedicationCodeableConcept()) {

            if (request.getMedicationCodeableConcept()
                    .hasText()) {

                medication.setMedicationName(
                    request.getMedicationCodeableConcept()
                            .getText()
                );

            } else if (request.getMedicationCodeableConcept()
                    .hasCoding()) {

                medication.setMedicationName(
                    request.getMedicationCodeableConcept()
                            .getCodingFirstRep()
                            .getDisplay()
                );
            }

        } else if (request.hasMedicationReference()) {

            medication.setMedicationName(
                request.getMedicationReference()
                        .getDisplay()
            );
        }


        // Dosage
        if (request.hasDosageInstruction()) {

            var dosage =
                    request.getDosageInstructionFirstRep();

            if (dosage.hasText()) {

                medication.setDosage(
                    dosage.getText()
                );
            }


            // Frequency
            if (dosage.hasTiming()
                    && dosage.getTiming().hasRepeat()) {

                var repeat =
                        dosage.getTiming().getRepeat();

                String frequency = "";

                if (repeat.hasFrequency()) {

                    frequency =
                        String.valueOf(
                            repeat.getFrequency()
                        );
                }

                if (repeat.hasPeriod()) {

                    frequency += " every "
                            + repeat.getPeriod();
                }

                if (repeat.hasPeriodUnit()) {

                    frequency += " "
                            + repeat.getPeriodUnit()
                                    .toCode();
                }

                medication.setFrequency(frequency);
            }


            // Route
            if (dosage.hasRoute()) {

                medication.setRoute(
                    dosage.getRoute()
                            .getText()
                );
            }
        }


        // Authored date
        if (request.hasAuthoredOn()) {

            medication.setAuthoredOn(
                request.getAuthoredOnElement()
                        .getValueAsString()
            );
        }


        return medication;
    }
}