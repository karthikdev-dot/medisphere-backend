package healthcare.DTO;





	public class MLPredictionRequest {

	    private double age;
	    private double hba1c;
	    private double glucose;
	    private double diabetesDuration;
	    private double bloodPressure;

	    public double getAge() {
	        return age;
	    }

	    public void setAge(double age) {
	        this.age = age;
	    }

	    public double getHba1c() {
	        return hba1c;
	    }

	    public void setHba1c(double hba1c) {
	        this.hba1c = hba1c;
	    }

	    public double getGlucose() {
	        return glucose;
	    }

	    public void setGlucose(double glucose) {
	        this.glucose = glucose;
	    }

	    public double getDiabetesDuration() {
	        return diabetesDuration;
	    }

	    public void setDiabetesDuration(double diabetesDuration) {
	        this.diabetesDuration = diabetesDuration;
	    }

	    public double getBloodPressure() {
	        return bloodPressure;
	    }

	    public void setBloodPressure(double bloodPressure) {
	        this.bloodPressure = bloodPressure;
	    }
	}

