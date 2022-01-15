package edu.pjatk.app.project.team;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantRole;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.request.TeamRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final ParticipantService participantService;
    private final ProjectRepository projectRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserService userService, ParticipantService participantService,
                       ProjectRepository projectRepository){
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.participantService = participantService;
        this.projectRepository = projectRepository;
    }


    public boolean addTeam(TeamRequest teamRequest){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(teamRequest.getProjectId());

        if (loggedUser.isPresent() && project.isPresent()){

            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    project.get().getId(), loggedUser.get().getId());

            if (participant.isPresent() && !participant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)){

                Set<Participant> participantsTeam = new HashSet<>();

                for (String username : teamRequest.getUsernames()){
                    participantsTeam.add(participantService.getParticipantByUsernameAndProjectId(
                            username, project.get().getId()).get()
                    );
                }

                teamRepository.add(new Team(teamRequest.getName(), teamRequest.getDescription(), participantsTeam, project.get()));
                return true;
            }
            else return false;
        }
        else return false;
    }

}
