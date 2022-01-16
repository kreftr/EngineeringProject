package edu.pjatk.app.project.team;

import edu.pjatk.app.request.TeamRequest;
import edu.pjatk.app.response.project.TeamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }


    @PostMapping(value = "/create")
    public ResponseEntity createTeam(@RequestBody TeamRequest teamRequest){
        if (teamService.addTeam(teamRequest)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PutMapping(value = "/edit/{teamId}")
    public ResponseEntity editTeam(@PathVariable Long teamId, @RequestBody TeamRequest teamRequest){
        if(teamService.editTeam(teamId, teamRequest)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/remove/{teamId}")
    public ResponseEntity removeTeam(@PathVariable Long teamId){
        if (teamService.removeTeam(teamId)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/getProjectTeams/{id}")
    public ResponseEntity getTeams(@PathVariable Long id){
        Set<TeamResponse> teams = teamService.getTeams(id);
        if (!teams.isEmpty()){
            return new ResponseEntity(teams, HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/addMember/{teamId}/{userId}/{projectId}")
    public ResponseEntity addMember(@PathVariable Long teamId, @PathVariable Long userId, @PathVariable Long projectId){
        if (teamService.addMember(teamId, userId, projectId)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/removeMember/{teamId}/{userId}/{projectId}")
    public ResponseEntity removeMember(@PathVariable Long teamId, @PathVariable Long userId, @PathVariable Long projectId){
        if (teamService.removeMember(teamId, userId, projectId)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

}
