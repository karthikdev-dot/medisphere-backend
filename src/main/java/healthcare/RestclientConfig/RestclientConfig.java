package healthcare.RestclientConfig;



	

	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.client.RestClient;

	@Configuration
	public class RestclientConfig {

	    @Bean
	    public RestClient restClient() {
	        return RestClient.builder()
	                .baseUrl("http://localhost:5000")
	                .build();
	    }
	}

