package healthcare.KafkaVitalEvents;





	import healthcare.Vitalsigns.VitalSigns;

	public class VitalEvents {

	    private String patientId;

	    private VitalSigns vitals;

	    public VitalEvents() {
	    }

	    public VitalEvents(String patientId, VitalSigns vitals) {
	        this.patientId = patientId;
	        this.vitals = vitals;
	    }

	    public String getPatientId() {
	        return patientId;
	    }

	    public void setPatientId(String patientId) {
	        this.patientId = patientId;
	    }

	    public VitalSigns getVitals() {
	        return vitals;
	    }

	    public void setVitals(VitalSigns vitals) {
	        this.vitals = vitals;
	    }
	}

