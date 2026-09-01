package healthcare.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medications")
public class PatientMedications {

    @Id
    private String id;

    private String patientId;

    private String medicationRequestId;

    private String medicationId;

    private String medicationName;

    private String status;

    private String dosage;
    
    private String period;
    
    private String periodUnit;

    private String doseValue;

    private String doseUnit;

    private String frequency;

    private String route;

    private String authoredOn;

    public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPeriodUnit() {
		return periodUnit;
	}

	public void setPeriodUnit(String periodUnit) {
		this.periodUnit = periodUnit;
	}

	public String getDoseValue() {
		return doseValue;
	}

	public void setDoseValue(String doseValue) {
		this.doseValue = doseValue;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMedicationRequestId() {
        return medicationRequestId;
    }

    public void setMedicationRequestId(String medicationRequestId) {
        this.medicationRequestId = medicationRequestId;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getAuthoredOn() {
        return authoredOn;
    }

    public void setAuthoredOn(String authoredOn) {
        this.authoredOn = authoredOn;
    }
}
