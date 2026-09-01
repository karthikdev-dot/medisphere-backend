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


    // GET ALL PATIENTS
    public List<Patient> getAllPatients() {

        List<Patient> patients = new ArrayList<>();

        Bundle bundle = fhirClient
                .search()
                .forResource(org.hl7.fhir.r4.model.Patient.class)
                .returnBundle(Bundle.class)
                .execute();
        
        System.out.println("FHIR entries = " + bundle.getEntry().size());

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {

            org.hl7.fhir.r4.model.Patient fhirPatient =
                    (org.hl7.fhir.r4.model.Patient) entry.getResource();
            
            System.out.println(
                    "FHIR Patient ID = "
                    + fhirPatient.getIdElement().getIdPart());

            Patient patient = convertToPatient(fhirPatient);

            patientRepository.save(patient);

            patients.add(patient);
        }

        return patients;
    }


    // GET PATIENT BY ID
    public String getPatientById(String id) {

        org.hl7.fhir.r4.model.Patient fhirPatient =
                fhirClient
                        .read()
                        .resource(org.hl7.fhir.r4.model.Patient.class)
                        .withId(id)
                        .execute();

        Patient patient = convertToPatient(fhirPatient);

        patientRepository.save(patient);

        return "Patient fetched and saved";
    }


    // COMMON EXTRACTION / CONVERSION METHOD
    private Patient convertToPatient(
            org.hl7.fhir.r4.model.Patient fhirPatient) {

        Patient patient = new Patient();


        // FHIR ID
        patient.setFhirId(
                fhirPatient.getIdElement().getIdPart()
        );


        // Gender
        if (fhirPatient.hasGender()) {

            patient.setGender(
                    fhirPatient.getGender().toCode()
            );
        }


        // Birth Date
        if (fhirPatient.hasBirthDate()) {

            patient.setBirthDate(
                    fhirPatient.getBirthDateElement()
                            .getValueAsString()
            );
        }


        // Name
        if (fhirPatient.hasName()) {

            String given = "";
            String family = "";

            if (fhirPatient.getNameFirstRep().hasGiven()) {

                given = fhirPatient.getNameFirstRep()
                        .getGivenAsSingleString();
            }

            if (fhirPatient.getNameFirstRep().hasFamily()) {

                family = fhirPatient.getNameFirstRep()
                        .getFamily();
            }

            patient.setName(given + " " + family);
        }

        return patient;
    }
}