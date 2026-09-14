package healthcare.FHIRService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import healthcare.DTO.CVDPredictionRequest;
import healthcare.DTO.CVDPredictionResponse;

@Service
public class CVDRiskPredictionService {

    @Autowired
    private RestClient restClient;

    public CVDPredictionResponse predict(CVDPredictionRequest request) {

        CVDPredictionResponse response = restClient
                .post()
                .uri("/cvd/predict")
                .body(request)
                .retrieve()
                .body(CVDPredictionResponse.class);

        return response;
    }
}


