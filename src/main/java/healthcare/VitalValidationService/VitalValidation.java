package healthcare.VitalValidationService;

import org.springframework.stereotype.Service;

import healthcare.Vitalsigns.VitalSigns;

@Service
public class VitalValidation{

    public boolean isValid(VitalSigns vitals) {

        if (vitals == null) {
            return false;
        }

        // Heart rate
        if (vitals.getHeartRate() <= 0) {
            return false;
        }

        // SpO2 must be between 0 and 100
        if (vitals.getSpo2() < 0 || vitals.getSpo2() > 100) {
            return false;
        }

        // Temperature
        if (vitals.getTemperature() < 30 ||
                vitals.getTemperature() > 45) {
            return false;
        }

        // Blood pressure
        if (vitals.getSystolicBP() <= 0 ||
                vitals.getDiastolicBP() <= 0) {
            return false;
        }

        // Respiratory rate
        if (vitals.getRespiratoryRate() <= 0) {
            return false;
        }

        // Glucose
        if (vitals.getGlucose() <= 0) {
            return false;
        }

        return true;
    }
}
