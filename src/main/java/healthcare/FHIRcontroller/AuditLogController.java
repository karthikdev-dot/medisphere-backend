package healthcare.FHIRcontroller;

	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.*;

	import healthcare.Entity.AuditLog;
	import healthcare.Repository.AuditLogRepo;

	@RestController
	@RequestMapping("/api")
	public class AuditLogController {

	    @Autowired
	    private AuditLogRepo auditLogRepo;

	    // Get all audit logs
	    @GetMapping
	    public List<AuditLog> getAllAuditLogs() {
	        return auditLogRepo.findAll();
	    }

	    // Get audit log by ID
	    @GetMapping("/{id}")
	    public AuditLog getAuditLogById(@PathVariable String id) {
	        return auditLogRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Audit log not found"));
	    }

	    // Create audit log
	    @PostMapping("/auditlog")
	    public AuditLog createAuditLog(@RequestBody AuditLog auditLog) {
	        return auditLogRepo.save(auditLog);
	    }

	    // Delete audit log
	    @DeleteMapping("/{id}")
	    public String deleteAuditLog(@PathVariable String id) {

	        if (!auditLogRepo.existsById(id)) {
	            return "Audit log not found";
	        }

	        auditLogRepo.deleteById(id);

	        return "Audit log deleted successfully";
	    }
	}

