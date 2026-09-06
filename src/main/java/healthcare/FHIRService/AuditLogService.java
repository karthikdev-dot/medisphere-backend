package healthcare.FHIRService;




	import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

	import healthcare.Entity.AuditLog;
	import healthcare.Repository.AuditLogRepo;

	@Service
	public class AuditLogService {

		
		@Autowired
	    private  AuditLogRepo auditLogRepo;

	  

	    public void createAuditLog(
	            String clinicianUser,
	            String userRole,
	            String department,
	            String actionPerformed,
	            String targetRecord,
	            String accessPoint,
	            String ipAddress,
	            String status) {

	        AuditLog auditLog = new AuditLog();

	        auditLog.setTimestamp(LocalDateTime.now());
	        auditLog.setClinicianUser(clinicianUser);
	        auditLog.setUserRole(userRole);
	        auditLog.setDepartment(department);
	        auditLog.setActionPerformed(actionPerformed);
	        auditLog.setTargetRecord(targetRecord);
	        auditLog.setAccessPoint(accessPoint);
	        auditLog.setIpAddress(ipAddress);
	        auditLog.setStatus(status);

	        auditLogRepo.save(auditLog);
	    }
	}

