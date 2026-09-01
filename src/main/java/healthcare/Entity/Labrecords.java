package healthcare.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "labRecords")
public class Labrecords {
	
	
	    @Id
	    private String id;
	    private String observationId;
	    private String patientId;

	    private String testName;
	    private String value;
	    private String unit;

	    private String status;
	    private String referenceRange;
	    private String effectiveDate;

	    public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getObservationId() {
			return observationId;
		}
		public void setObservationId(String observationId) {
			this.observationId = observationId;
		}
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
		public String getTestName() {
			return testName;
		}
		public void setTestName(String testName) {
			this.testName = testName;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getReferenceRange() {
			return referenceRange;
		}
		public void setReferenceRange(String referenceRange) {
			this.referenceRange = referenceRange;
		}
		public String getEffectiveDate() {
			return effectiveDate;
		}
		public void setEffectiveDate(String effectiveDate) {
			this.effectiveDate = effectiveDate;
		}
		
	}

