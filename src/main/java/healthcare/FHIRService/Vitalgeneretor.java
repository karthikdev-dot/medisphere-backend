package healthcare.FHIRService;





	import healthcare.Vitalsigns.*;
	import org.springframework.stereotype.Service;

	import java.time.LocalDateTime;
	import java.util.Random;

	@Service
	public class Vitalgeneretor {

	    private final Random random = new Random();

	    public VitalSigns generateVitals() {

	        VitalSigns vitals = new VitalSigns();

	        vitals.setHeartRate(145);
	        
	        vitals.setSpo2(
	                random.nextInt(4) + 96
	        );

	        vitals.setTemperature(
	                36.3 + (random.nextDouble() * 1.2)
	        );

	        vitals.setSystolicBP(
	                random.nextInt(31) + 110
	        );

	        vitals.setDiastolicBP(
	                random.nextInt(21) + 70
	        );

	        vitals.setRespiratoryRate(
	                random.nextInt(7) + 12
	        );

	        vitals.setGlucose(
	                random.nextInt(41) + 80
	        );

	        vitals.setTimestamp(
	                LocalDateTime.now()
	        );

	        return vitals;
	    }
	}
	

