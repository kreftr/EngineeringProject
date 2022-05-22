package edu.pjatk.app.report.blockade;

import edu.pjatk.app.request.BlockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blockade")
public class BlockadeController {

    private final BlockadeService blockadeService;

    @Autowired
    public BlockadeController(BlockadeService blockadeService) {
        this.blockadeService = blockadeService;
    }

    @PostMapping
    public ResponseEntity block(@RequestBody BlockRequest request) {
        BlockadeResultEnum result = blockadeService.block(request);
        if (BlockadeResultEnum.BLOCKED.equals(result)) {
            return new ResponseEntity(result.name(), HttpStatus.OK);
        } else {
            return new ResponseEntity(result.name(), HttpStatus.CONFLICT);
        }
    }

}
