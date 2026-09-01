package healthcare.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "patients")
	public class Patient {

	    @Id
	    private String id;
	    private String fhirId;
	    private String name;
	    private String gender;
	    private String birthDate;
		public String getBirthDate() {
			return birthDate;
		}
		public void setBirthDate(String birthDate) {
			this.birthDate = birthDate;
		}
		public String getFhirId() {
			return fhirId;
		}
		public void setFhirId(String fhirId) {
			this.fhirId = fhirId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	    // getters and setters
	}

