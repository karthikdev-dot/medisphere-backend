package healthcare.FHIRService;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import healthcare.Entity.Patient;
import healthcare.Repository.PatientRepo;

@Service
public class PatientService {

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private PatientRepo patientRepository;


    // ==========================================
    // GET MAXIMUM 100 PATIENTS FROM FHIR
    // ==========================================
    public List<Patient> getAllPatients() {

        List<Patient> patients = new ArrayList<>();

        // Maximum number of NEW patients to import
        int maxPatients = 100;

        // Get first page from FHIR
        Bundle bundle = fhirClient
                .search()
                .forResource(org.hl7.fhir.r4.model.Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        int patientNumber = 1;
        int pageNumber = 1;


        // Continue until we get 100 patients
        // or there are no more FHIR pages
        while (bundle != null && patients.size() < maxPatients) {

            System.out.println(
                    "========== FHIR PAGE "
                    + pageNumber
                    + " =========="
            );

            System.out.println(
                    "Patients in page: "
                    + bundle.getEntry().size()
            );


            // ==========================================
            // READ PATIENTS FROM CURRENT PAGE
            // ==========================================
            for (Bundle.BundleEntryComponent entry
                    : bundle.getEntry()) {

                // Stop when 100 NEW patients are saved
                if (patients.size() >= maxPatients) {
                    break;
                }


                org.hl7.fhir.r4.model.Patient fhirPatient =
                        (org.hl7.fhir.r4.model.Patient)
                                entry.getResource();


                // Get original FHIR ID
                String fhirId =
                        fhirPatient
                                .getIdElement()
                                .getIdPart();


                System.out.println(
                        "FHIR Patient ID = "
                        + fhirId
                );


                // ==========================================
                // CHECK DUPLICATE
                // ==========================================
                Patient existingPatient =
                        patientRepository
                                .findByFhirId(fhirId);


                if (existingPatient == null) {

                    // Create MongoDB patient
                    Patient patient =
                            convertToPatient(
                                    fhirPatient,
                                    patientRepository.count() + 1
                            );


                    // Save to MongoDB
                    patientRepository.save(patient);

                    patients.add(patient);


                    System.out.println(
                            "Saved = "
                            + patient.getName()
                    );

                } else {

                    System.out.println(
                            "Already exists = "
                            + fhirId
                    );
                }
            }


            // ==========================================
            // STOP IF 100 PATIENTS ARE SAVED
            // ==========================================
            if (patients.size() >= maxPatients) {
                break;
            }


            // ==========================================
            // LOAD NEXT FHIR PAGE
            // ==========================================
            if (bundle.getLink(Bundle.LINK_NEXT) != null) {

                String nextUrl =
                        bundle
                                .getLink(Bundle.LINK_NEXT)
                                .getUrl();


                System.out.println(
                        "Loading next FHIR page..."
                );


                bundle = fhirClient
                        .loadPage()
                        .byUrl(nextUrl)
                        .andReturnBundle(Bundle.class)
                        .execute();


                pageNumber++;

            } else {

                // No more pages
                bundle = null;

                System.out.println(
                        "========== NO MORE FHIR PAGES =========="
                );
            }
        }


        // ==========================================
        // FINAL RESULT
        // ==========================================
        System.out.println(
                "======================================"
        );

        System.out.println(
                "NEW PATIENTS SAVED = "
                + patients.size()
        );

        System.out.println(
                "TOTAL PATIENTS IN MONGODB = "
                + patientRepository.count()
        );

        System.out.println(
                "======================================"
        );


        return patients;
    }


    // ==========================================
    // GET PATIENT BY FHIR ID
    // ==========================================
    public String getPatientById(String id) {

        org.hl7.fhir.r4.model.Patient fhirPatient =
                fhirClient
                        .read()
                        .resource(
                                org.hl7.fhir.r4.model.Patient.class
                        )
                        .withId(id)
                        .execute();


        String fhirId =
                fhirPatient
                        .getIdElement()
                        .getIdPart();


        // Check if already exists
        Patient existingPatient =
                patientRepository
                        .findByFhirId(fhirId);


        if (existingPatient != null) {

            return "Patient already exists in MongoDB";
        }


        // Save new patient
        Patient patient =
                convertToPatient(
                        fhirPatient,
                        (int) patientRepository.count() + 1
                );


        patientRepository.save(patient);


        return "Patient fetched and saved";
    }


    // ==========================================
    // CONVERT FHIR PATIENT → MONGODB PATIENT
    // ==========================================
    private Patient convertToPatient(
            org.hl7.fhir.r4.model.Patient fhirPatient,
            long patientNumber) {

        Patient patient = new Patient();


        // ==========================================
        // KEEP ORIGINAL FHIR ID
        // ==========================================
        patient.setFhirId(
                fhirPatient
                        .getIdElement()
                        .getIdPart()
        );


        // ==========================================
        // GENDER
        // ==========================================
        if (fhirPatient.hasGender()) {

            patient.setGender(
                    fhirPatient
                            .getGender()
                            .toCode()
            );
        }


        // ==========================================
        // BIRTH DATE
        // ==========================================
        if (fhirPatient.hasBirthDate()) {

            patient.setBirthDate(
                    fhirPatient
                            .getBirthDateElement()
                            .getValueAsString()
            );
        }


        // ==========================================
        // CUSTOM NAME
        // ==========================================
        patient.setName(
                "Patient " + patientNumber
        );


        return patient;
    }
}