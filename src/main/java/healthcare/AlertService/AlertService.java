package healthcare.AlertService;

import healthcare.Alert.AlertEntity;
import healthcare.AlertMetric.AlertMetric;
import healthcare.AlertRepo.AlertRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepo alertRepo;


    // ==========================================
    // GET ALL ALERTS
    // ==========================================

    public List<AlertEntity> getAllAlerts() {

        return alertRepo.findAll();
    }


    // ==========================================
    // GET ALERTS FOR A PARTICULAR PATIENT
    // ==========================================

    public List<AlertEntity> getAlertsByPatient(
            String patientId) {

        return alertRepo.findByPatientId(patientId);
    }


    // ==========================================
    // GET ALERTS BASED ON STATUS
    // ==========================================

    public List<AlertEntity> getAlertsByStatus(
            String status) {

        return alertRepo.findByStatus(status);
    }


    // ==========================================
    // GET ACTIVE ALERTS FOR A PATIENT
    // ==========================================

    public List<AlertEntity> getActiveAlerts(
            String patientId) {

        return alertRepo
                .findByPatientIdAndStatus(
                        patientId,
                        "NEW"
                );
    }


    // ==========================================
    // ACKNOWLEDGE ALERT
    // ==========================================

    public AlertEntity acknowledgeAlert(
            String alertId,
            String acknowledgedBy) {

        AlertEntity alert = alertRepo
                .findById(alertId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Alert not found"
                        )
                );


        alert.setStatus("ACKNOWLEDGED");

        alert.setAcknowledgedBy(
                acknowledgedBy
        );

        alert.setAcknowledgedAt(
                LocalDateTime.now()
        );


        return alertRepo.save(alert);
    }
        
     // ==========================================
     // CLASSIFY ALERT
     // ==========================================

     public AlertEntity classifyAlert(
             String alertId,
             String classification,
             String classifiedBy) {

         AlertEntity alert = alertRepo
                 .findById(alertId)
                 .orElseThrow(() ->
                         new RuntimeException(
                                 "Alert not found"
                         )
                 );

         if (!classification.equals("VALID")
                 && !classification.equals("FALSE_POSITIVE")) {

             throw new RuntimeException(
                     "Classification must be VALID or FALSE_POSITIVE"
             );
         }

         alert.setClassification(classification);

         alert.setClassifiedBy(
                 classifiedBy
         );

         alert.setClassifiedAt(
                 LocalDateTime.now()
         );

         return alertRepo.save(alert);
     }
     
        
         public AlertMetric getAlertMetrics() {

        	    long validAlerts =
        	            alertRepo.countByClassification("VALID");

        	    long falsePositiveAlerts =
        	            alertRepo.countByClassification("FALSE_POSITIVE");

        	    long totalClassifiedAlerts =
        	            validAlerts + falsePositiveAlerts;

        	    double precision = 0.0;

        	    if (totalClassifiedAlerts > 0) {
        	        precision =
        	                ((double) validAlerts / totalClassifiedAlerts) * 100;
        	    }

        	    AlertMetric metrics = new AlertMetric();

        	    metrics.setTotalClassifiedAlerts(
        	            totalClassifiedAlerts
        	    );

        	    metrics.setValidAlerts(
        	            validAlerts
        	    );

        	    metrics.setFalsePositiveAlerts(
        	            falsePositiveAlerts
        	    );

        	    metrics.setPrecision(
        	            precision
        	    );

        	    return metrics;
        	
        
    }
}

