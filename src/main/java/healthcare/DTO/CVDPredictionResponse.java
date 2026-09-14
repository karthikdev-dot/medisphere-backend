package healthcare.DTO;



	import java.util.Map;

	public class CVDPredictionResponse {

	    private String result;
	    private String riskLevel;
	    private Double riskPercentage;
	    private String model;
	    private String version;
	    private int federatedRound;
	    private Map<String, Object> features;

	    public String getResult() {
	        return result;
	    }

	    public void setResult(String result) {
	        this.result = result;
	    }

	    public String getRiskLevel() {
	        return riskLevel;
	    }

	    public void setRiskLevel(String riskLevel) {
	        this.riskLevel = riskLevel;
	    }

	    public Double getRiskPercentage() {
	        return riskPercentage;
	    }

	    public void setRiskPercentage(Double riskPercentage) {
	        this.riskPercentage = riskPercentage;
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

	    public Map<String, Object> getFeatures() {
	        return features;
	    }

	    public void setFeatures(Map<String, Object> features) {
	        this.features = features;
	    }
	}


