package edu.pjatk.app.report;

import edu.pjatk.app.request.ReportRequest;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity report(@RequestBody ReportRequest reportRequest) {
        ReportResultEnum result = reportService.report(reportRequest);
        if (ReportResultEnum.REPORTED.equals(result)) {
            return new ResponseEntity(new ResponseMessage("Reported"),HttpStatus.OK);
        } else if (ReportResultEnum.ALREADY_REPORTED.equals(result)) {
            return new ResponseEntity(new ResponseMessage("Already reported"),HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity(new ResponseMessage("Error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getReports() {
        List<Report> reports = reportService.getReports();
        if (!reports.isEmpty()) {
            return new ResponseEntity(reports, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

}
