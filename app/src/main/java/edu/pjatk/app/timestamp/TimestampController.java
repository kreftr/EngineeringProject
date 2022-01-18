package edu.pjatk.app.timestamp;


import edu.pjatk.app.request.TimestampRequest;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/time")
public class TimestampController {
    private final TimestampService timestampService;

    @Autowired
    public TimestampController(TimestampService timestampService) {
        this.timestampService = timestampService;
    }

    @GetMapping(value = "/getTimestampById/{id}")
    public ResponseEntity<?> getTimestampById(@PathVariable Long id) {
        Optional<Timestamp> timestamp = timestampService.getTimestampById(id);
        if (timestamp.isPresent())
        {
            return new ResponseEntity<>(
                    timestamp, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Timestamp with given id not found"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getUserTimestampsForProject/{project_id}")
    public ResponseEntity<?> getUserTimestampsForProject(@PathVariable Long project_id) {
        Optional<List<Timestamp>> timestamps= timestampService.getUserTimestampsForProject(project_id);
        if (timestamps.isPresent())
        {
            return new ResponseEntity<>(
                    timestamps, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no timestamps for this project yet!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/addTimestamp")
    public ResponseEntity<?> addTimestamp(@Valid @RequestPart TimestampRequest timestampRequest) {
        timestampService.addTimestamp(timestampRequest);
        return new ResponseEntity<>(
                new ResponseMessage("Timestamp added!"), HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/deleteTimestampById/{id}")
    public ResponseEntity<?> deleteTimestampById(@PathVariable long id) {
        timestampService.deleteTimestampById(id);
        return new ResponseEntity<>(
                new ResponseMessage("Timestamp deleted!"), HttpStatus.OK
        );
    }
}
