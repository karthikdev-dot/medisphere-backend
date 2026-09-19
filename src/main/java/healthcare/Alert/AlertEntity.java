package healthcare.Alert;



	import org.springframework.data.annotation.Id;
	import org.springframework.data.mongodb.core.mapping.Document;

	import java.time.LocalDateTime;

	@Document(collection = "alerts")
	public class AlertEntity {

	    @Id
	    private String id;

	    private String patientId;
	    private String alertType;
	    private String message;
	    private String severity;
	    private String status;

	    private LocalDateTime createdAt;

	    private String acknowledgedBy;
	    private LocalDateTime acknowledgedAt;

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

	    public String getAlertType() {
	        return alertType;
	    }

	    public void setAlertType(String alertType) {
	        this.alertType = alertType;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public String getSeverity() {
	        return severity;
	    }

	    public void setSeverity(String severity) {
	        this.severity = severity;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }

	    public String getAcknowledgedBy() {
	        return acknowledgedBy;
	    }

	    public void setAcknowledgedBy(String acknowledgedBy) {
	        this.acknowledgedBy = acknowledgedBy;
	    }

	    public LocalDateTime getAcknowledgedAt() {
	        return acknowledgedAt;
	    }

	    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
	        this.acknowledgedAt = acknowledgedAt;
	    }
	}

