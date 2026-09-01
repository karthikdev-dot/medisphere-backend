package healthcare.FHIRcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import healthcare.Entity.Labrecords;
import healthcare.FHIRService.LabRecordService;

@RestController
@RequestMapping("/labs")
public class LabrecordControlller {

    @Autowired
    private LabRecordService labRecordService;

    @GetMapping("/fetch")
    public List<Labrecords> fetchLabs() {

        return labRecordService
                .fetchAndSaveLabRecords();
    }

    @GetMapping
    public List<Labrecords> getAllLabRecords() {

        return labRecordService
                .getAllLabRecords();
    }


    // =====================================================
    // GET LAB RECORD BY MONGODB ID
    // =====================================================

    @GetMapping("/{id}")
    public Labrecords getLabRecordById(
            @PathVariable String id) {

        return labRecordService
                .getLabRecordById(id);
    }
}
