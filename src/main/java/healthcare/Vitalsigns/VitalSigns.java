package healthcare.Vitalsigns;


	import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

	
	@Document(collection = "vital_signs")
	public class VitalSigns {

		
        @Id
		private String patientId;
	    private int heartRate;

	    private int spo2;

	    private double temperature;

	    private int systolicBP;

	    private int diastolicBP;

	    private int respiratoryRate;

	    private int glucose;

	    private LocalDateTime timestamp;
	    
	    public LocalDateTime getRecordedAt() {
			return recordedAt;
		}

		public void setRecordedAt(LocalDateTime recordedAt) {
			this.recordedAt = recordedAt;
		}

		private LocalDateTime recordedAt;

	    public VitalSigns() {
	    }

	    public int getHeartRate() {
	        return heartRate;
	    }

	    public void setHeartRate(int heartRate) {
	        this.heartRate = heartRate;
	    }

	    public int getSpo2() {
	        return spo2;
	    }

	    public void setSpo2(int spo2) {
	        this.spo2 = spo2;
	    }

	    public double getTemperature() {
	        return temperature;
	    }

	    public void setTemperature(double temperature) {
	        this.temperature = temperature;
	    }

	    public int getSystolicBP() {
	        return systolicBP;
	    }

	    public void setSystolicBP(int systolicBP) {
	        this.systolicBP = systolicBP;
	    }

	    public int getDiastolicBP() {
	        return diastolicBP;
	    }

	    public void setDiastolicBP(int diastolicBP) {
	        this.diastolicBP = diastolicBP;
	    }

	    public int getRespiratoryRate() {
	        return respiratoryRate;
	    }

	    public void setRespiratoryRate(int respiratoryRate) {
	        this.respiratoryRate = respiratoryRate;
	    }

	    public int getGlucose() {
	        return glucose;
	    }

	    public void setGlucose(int glucose) {
	        this.glucose = glucose;
	    }

	    public LocalDateTime getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(LocalDateTime timestamp) {
	        this.timestamp = timestamp;
	    }
	    

		 public String getPatientId() {
			return patientId;
		}

		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
	}

