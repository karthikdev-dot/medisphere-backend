package healthcare.FHIRcontroller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import healthcare.Entity.Labrecords;
import healthcare.FHIRService.LabRecordService;

@RestController
@RequestMapping("/api/labrecords")
public class LabrecordController {

    @Autowired
    private LabRecordService labRecordService;

    @GetMapping("/fetch")
    public List<Labrecords> fetchAndSaveLabRecords() {

        return labRecordService.fetchAndSaveLabRecords();
    }
}
