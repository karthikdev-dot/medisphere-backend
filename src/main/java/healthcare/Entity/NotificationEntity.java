package healthcare.Entity;



	import org.springframework.data.annotation.Id;
	import org.springframework.data.mongodb.core.mapping.Document;

	import java.time.LocalDateTime;

	@Document(collection = "notifications")
	public class NotificationEntity {

	    @Id
	    private String id;

	    private String patientId;
	    private String alertId;
	    private String alertType;
	    private String message;
	    private String severity;

	    private String recipient;
	    private String status;

	    private LocalDateTime createdAt;
	    private LocalDateTime readAt;
	    
	    public NotificationEntity(
	            String patientId,
	            String alertType,
	            String message,
	            String severity) {

	        this.patientId = patientId;
	        this.alertType = alertType;
	        this.message = message;
	        this.severity = severity;
	    }

	    public NotificationEntity() {
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

	    public String getAlertId() {
	        return alertId;
	    }

	    public void setAlertId(String alertId) {
	        this.alertId = alertId;
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

	    public String getRecipient() {
	        return recipient;
	    }

	    public void setRecipient(String recipient) {
	        this.recipient = recipient;
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

	    public LocalDateTime getReadAt() {
	        return readAt;
	    }

	    public void setReadAt(LocalDateTime readAt) {
	        this.readAt = readAt;
	    }
	}

