package healthcare.FHIRService;


	


	import healthcare.Alert.AlertEntity;
	import org.springframework.stereotype.Service;

	@Service
	public class AlertRoutingService {

	    public void routeAlert(AlertEntity alert) {

	        if (alert == null) {
	            return;
	        }

	        String severity = alert.getSeverity();

	        if ("CRITICAL".equalsIgnoreCase(severity)) {

	            System.out.println(
	                    "ALERT ROUTED → DOCTOR"
	            );

	        } else if ("HIGH".equalsIgnoreCase(severity)) {

	            System.out.println(
	                    "ALERT ROUTED → NURSE"
	            );

	        } else {

	            System.out.println(
	                    "ALERT ROUTED → ROUTINE MONITORING"
	            );
	        }
	    }
	}

