package healthcare.AlertController;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import healthcare.Alert.AlertEntity;
import healthcare.AlertMetric.AlertMetric;
import healthcare.AlertService.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin
public class AlertController {

    @Autowired
    private AlertService alertService;


    // Get all alerts
    @GetMapping
    public List<AlertEntity> getAllAlerts() {

        return alertService.getAllAlerts();
    }


    // Get alerts for a patient
    @GetMapping("/patient/{patientId}")
    public List<AlertEntity> getAlertsByPatient(
            @PathVariable String patientId) {

        return alertService.getAlertsByPatient(patientId);
    }


    // Get alerts by status
    @GetMapping("/status/{status}")
    public List<AlertEntity> getAlertsByStatus(
            @PathVariable String status) {

        return alertService.getAlertsByStatus(status);
    }


    // Acknowledge alert
    @PutMapping("/{alertId}/acknowledge")
    public AlertEntity acknowledgeAlert(
            @PathVariable String alertId,
            @RequestParam String acknowledgedBy) {

        return alertService.acknowledgeAlert(
                alertId,
                acknowledgedBy
                
        );
    }
        
        @PutMapping("/{alertId}/classify")
        public AlertEntity classifyAlert(
                @PathVariable String alertId,
                @RequestParam String classification,
                @RequestParam String classifiedBy) {

            return alertService.classifyAlert(
                    alertId,
                    classification,
                    classifiedBy
            );
        }
            
            @GetMapping("/metrics/precision")
            public AlertMetric getAlertMetrics() {

                return alertService.getAlertMetrics();
            
        
    }
}
