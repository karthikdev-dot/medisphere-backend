package healthcare.DTO;




	public class MLPredictionResponse {

	    private double riskPercentage;
	    private String riskLevel;
	    private String model;
	    private String version;
	    private int federatedRound;
	    private Features features;

	    public double getRiskPercentage() {
	        return riskPercentage;
	    }

	    public void setRiskPercentage(double riskPercentage) {
	        this.riskPercentage = riskPercentage;
	    }

	    public String getRiskLevel() {
	        return riskLevel;
	    }

	    public void setRiskLevel(String riskLevel) {
	        this.riskLevel = riskLevel;
	    }

	    public String getModel() {
	        return model;
	    }

	    public void setModel(String model) {
	        this.model = model;
	    }

	    public String getVersion() {
	        return version;
	    }

	    public void setVersion(String version) {
	        this.version = version;
	    }

	    public int getFederatedRound() {
	        return federatedRound;
	    }

	    public void setFederatedRound(int federatedRound) {
	        this.federatedRound = federatedRound;
	    }

	    public Features getFeatures() {
	        return features;
	    }

	    public void setFeatures(Features features) {
	        this.features = features;
	    }

	    public static class Features {

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
	}

