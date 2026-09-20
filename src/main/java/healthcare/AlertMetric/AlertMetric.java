package healthcare.AlertMetric;



	public class AlertMetric{

	    private long totalClassifiedAlerts;
	    private long validAlerts;
	    private long falsePositiveAlerts;
	    private double precision;

	    public long getTotalClassifiedAlerts() {
	        return totalClassifiedAlerts;
	    }

	    public void setTotalClassifiedAlerts(long totalClassifiedAlerts) {
	        this.totalClassifiedAlerts = totalClassifiedAlerts;
	    }

	    public long getValidAlerts() {
	        return validAlerts;
	    }

	    public void setValidAlerts(long validAlerts) {
	        this.validAlerts = validAlerts;
	    }

	    public long getFalsePositiveAlerts() {
	        return falsePositiveAlerts;
	    }

	    public void setFalsePositiveAlerts(long falsePositiveAlerts) {
	        this.falsePositiveAlerts = falsePositiveAlerts;
	    }

	    public double getPrecision() {
	        return precision;
	    }

	    public void setPrecision(double precision) {
	        this.precision = precision;
	    }
	}

