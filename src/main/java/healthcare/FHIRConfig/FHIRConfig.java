package healthcare.FHIRConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Configuration
public class FHIRConfig {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public IGenericClient fhirClient(FhirContext fhirContext) {

        return fhirContext.newRestfulGenericClient(
                
        		 "https://hapi.fhir.org/baseR4"
        );
    }
}
