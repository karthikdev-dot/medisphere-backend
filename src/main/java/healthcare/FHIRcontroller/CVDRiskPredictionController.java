package healthcare.FHIRcontroller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import healthcare.DTO.CVDPredictionRequest;
import healthcare.DTO.CVDPredictionResponse;
import healthcare.FHIRService.CVDRiskPredictionService;

@RestController
@RequestMapping("/api/cvd")
public class CVDRiskPredictionController {

    @Autowired
    private CVDRiskPredictionService cvdRiskPredictionService;

    @PostMapping("/predict")
    public CVDPredictionResponse predict(
            @RequestBody CVDPredictionRequest request) {

        return cvdRiskPredictionService.predict(request);
    }
}

