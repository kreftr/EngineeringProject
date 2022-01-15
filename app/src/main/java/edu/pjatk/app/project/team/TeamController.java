package edu.pjatk.app.project.team;

import edu.pjatk.app.request.TeamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }


    @PostMapping(value = "/add")
    public ResponseEntity addTeam(@RequestBody TeamRequest teamRequest){
        if (teamService.addTeam(teamRequest)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

}
