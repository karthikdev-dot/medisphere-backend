package healthcare.AnomolyDetectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthcare.Alert.AlertEntity;
import healthcare.AlertRepo.AlertRepo;
import healthcare.Vitalsigns.VitalSigns;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnomolyDetectionService {

	@Autowired
	private AlertRepo alertRepo;

	public void checkVitals(VitalSigns vitals) {

		if (vitals == null) {
			return;
		}

		// =================================
		// HIGH HEART RATE
		// =================================

		if (vitals.getHeartRate() > 120) {

			createAlert(vitals.getPatientId(), "HIGH_HEART_RATE", "Heart rate is " + vitals.getHeartRate() + " bpm",
					"HIGH");
		}

		// =================================
		// LOW SPO2
		// =================================

		if (vitals.getSpo2() < 90) {

			createAlert(vitals.getPatientId(), "LOW_SPO2", "SpO2 is " + vitals.getSpo2() + "%", "CRITICAL");
		}

		// =================================
		// HIGH TEMPERATURE
		// =================================

		if (vitals.getTemperature() > 39) {

			createAlert(vitals.getPatientId(), "HIGH_TEMPERATURE", "Temperature is " + vitals.getTemperature() + "°C",
					"HIGH");
		}

		// =================================
		// HIGH BLOOD PRESSURE
		// =================================

		if (vitals.getSystolicBP() > 180) {

			createAlert(vitals.getPatientId(), "HIGH_BLOOD_PRESSURE", "Systolic BP is " + vitals.getSystolicBP(),
					"CRITICAL");
		}
	}

	// ==========================================
	// CREATE ALERT + ALERT FATIGUE PREVENTION
	// ==========================================

	private void createAlert(String patientId, String alertType, String message, String severity) {

		// Check if an active NEW alert already exists
		Optional<AlertEntity> existingAlert = alertRepo.findByPatientIdAndAlertTypeAndStatus(patientId, alertType,
				"NEW");

		// ==========================================
		// DUPLICATE ALERT FOUND
		// ==========================================

		if (existingAlert.isPresent()) {

			System.out.println("ALERT SUPPRESSED - Duplicate alert detected");

			System.out.println("Patient: " + patientId + " | Type: " + alertType);

			return;
		}

		// ==========================================
		// CREATE NEW ALERT
		// ==========================================

		AlertEntity alert = new AlertEntity();

		alert.setPatientId(patientId);

		alert.setAlertType(alertType);

		alert.setMessage(message);

		alert.setSeverity(severity);

		alert.setStatus("NEW");

		alert.setCreatedAt(LocalDateTime.now());

		alertRepo.save(alert);

		System.out.println("ALERT CREATED: " + patientId + " | " + alertType + " | " + severity);
	}
}
