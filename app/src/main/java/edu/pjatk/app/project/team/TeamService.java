package edu.pjatk.app.project.team;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantRole;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.request.TeamRequest;
import edu.pjatk.app.response.project.MemberResponse;
import edu.pjatk.app.response.project.TeamResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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


    @Transactional
    public boolean addTeam(TeamRequest teamRequest){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(teamRequest.getProjectId());

        if (loggedUser.isPresent() && project.isPresent()){

            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), project.get().getId());

            if (participant.isPresent() && !participant.get().getParticipantRole()
                    .equals(ParticipantRole.PARTICIPANT)){

                Set<Participant> participantsTeam = new HashSet<>();

                for (String username : teamRequest.getUsernames()){
                    participantsTeam.add(participantService.getParticipantByUsernameAndProjectId(
                            username, project.get().getId()).get()
                    );
                }

                teamRepository.add(new Team(teamRequest.getName(), teamRequest.getDescription(),
                        participantsTeam, project.get()));

                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean editTeam(Long teamId, TeamRequest teamRequest){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Team> team = teamRepository.getById(teamId);

        if (loggedUser.isPresent() && team.isPresent()){
            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), team.get().getProject().getId()
            );

            if (participant.isPresent() && !participant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)){
                team.get().setName(teamRequest.getName());
                team.get().setDescription(teamRequest.getDescription());
                teamRepository.update(team.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean removeTeam(Long teamId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Team> team = teamRepository.getById(teamId);

        if (loggedUser.isPresent() && team.isPresent()){
            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), team.get().getProject().getId()
            );

            if (participant.isPresent() && !participant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)){
                teamRepository.remove(team.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

    public Optional<Team> getTeam(Long teamId){
        return teamRepository.getById(teamId);
    }

    public Set<TeamResponse> getTeams(Long projectId){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(projectId);

        if (loggedUser.isPresent() && project.isPresent()){
            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), project.get().getId());

            if (participant.isPresent()){

                Optional<List<Team>> teams = teamRepository.getByProjectId(projectId);

                if (teams.isPresent() && teams.get().size() > 0){
                    Set<TeamResponse> teamResponse = new HashSet<>();
                    Set<MemberResponse> participants = new HashSet<>();
                    String profilePhoto;

                    for (Team team : teams.get()){

                        for (Participant p : team.getParticipants()){
                            if (p.getUser().getProfile().getPhoto() != null) {
                                profilePhoto = p.getUser().getProfile().getPhoto().getFileName();
                            } else {
                                profilePhoto = null;
                            }

                            participants.add(
                                    new MemberResponse(p.getUser().getId(), p.getUser().getUsername(),
                                            profilePhoto, p.getParticipantRole().toString())
                            );
                        }
                        teamResponse.add(
                                new TeamResponse(team.getId(), team.getName(), team.getDescription(), new HashSet<>(participants))
                        );
                        participants.clear();
                    }
                    return teamResponse;
                }
                else return Collections.emptySet();
            }
            else return Collections.emptySet();
        }
        else return Collections.emptySet();
    }

    @Transactional
    public boolean addMember(Long teamId, Long userId, Long projectId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<User> userToAdd = userService.findUserById(userId);
        Optional<Team> team = teamRepository.getById(teamId);

        if (loggedUser.isPresent() && project.isPresent() && userToAdd.isPresent() && team.isPresent()){

            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), projectId
            );
            Optional<Participant> userToAddParticipant = participantService.getParticipantByUserAndProject(
                    userId, projectId
            );

            if (loggedParticipant.isPresent() && userToAddParticipant.isPresent() &&
                    !loggedParticipant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT) &&
                    !team.get().getParticipants().contains(userToAddParticipant.get())){

                team.get().getParticipants().add(userToAddParticipant.get());
                teamRepository.update(team.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean removeMember(Long teamId, Long userId, Long projectId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<User> userToRemove = userService.findUserById(userId);
        Optional<Team> team = teamRepository.getById(teamId);

        if (loggedUser.isPresent() && project.isPresent() && userToRemove.isPresent() && team.isPresent()){

            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), projectId
            );
            Optional<Participant> userToRemoveParticipant = participantService.getParticipantByUserAndProject(
                    userId, projectId
            );

            if (loggedParticipant.isPresent() && userToRemoveParticipant.isPresent() &&
                    !loggedParticipant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT) &&
                    team.get().getParticipants().contains(userToRemoveParticipant.get())){

                team.get().getParticipants().remove(userToRemoveParticipant.get());
                teamRepository.update(team.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

}
