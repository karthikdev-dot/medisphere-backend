package healthcare.Entity;





	import java.time.LocalDateTime;

	import org.springframework.data.annotation.Id;
	import org.springframework.data.mongodb.core.mapping.Document;



	
	@Document(collection = "audit_logs")
	public class AuditLog {

	    @Id
	    private String id;
	    private LocalDateTime timestamp;

	    private String clinicianUser;

	    private String userRole;

	    private String department;

	    private String actionPerformed;

	    private String targetRecord;

	    private String accessPoint;

	    private String ipAddress;

	    private String status;

	    public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}

		public String getClinicianUser() {
			return clinicianUser;
		}

		public void setClinicianUser(String clinicianUser) {
			this.clinicianUser = clinicianUser;
		}

		public String getUserRole() {
			return userRole;
		}

		public void setUserRole(String userRole) {
			this.userRole = userRole;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getActionPerformed() {
			return actionPerformed;
		}

		public void setActionPerformed(String actionPerformed) {
			this.actionPerformed = actionPerformed;
		}

		public String getTargetRecord() {
			return targetRecord;
		}

		public void setTargetRecord(String targetRecord) {
			this.targetRecord = targetRecord;
		}

		public String getAccessPoint() {
			return accessPoint;
		}

		public void setAccessPoint(String accessPoint) {
			this.accessPoint = accessPoint;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		
	}
