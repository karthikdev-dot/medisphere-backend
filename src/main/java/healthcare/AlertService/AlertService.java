package healthcare.AlertService;

import healthcare.Alert.AlertEntity;
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
}

