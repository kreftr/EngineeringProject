package edu.pjatk.app.project.participant;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, ProjectService projectService,
                              UserService userService, ProjectRepository projectRepository){
        this.participantRepository = participantRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public void updateParticipant(Participant participant){
        participantRepository.update(participant);
    }

    public void removeParticipant(Participant participant){
        participantRepository.remove(participant);
    }

    public Optional<Participant> getById(Long id){
        return participantRepository.getById(id);
    }

    public Optional<Participant> getParticipantByUserAndProject(Long userid, Long projectId){
        return participantRepository.getByUserAndProjectIds(userid, projectId);
    }

    public Optional<Participant> getParticipantByUsernameAndProjectId(String username, Long projectId){
        return participantRepository.getByUsernameAndProjectId(username, projectId);
    }

    public Optional<List<Participant>> getAllWhereUserJoined(Long userId){
        return participantRepository.getAllWhereUserJoined(userId);
    }

    public Optional<List<Participant>> getAllPending(Long userId){
        return participantRepository.getAllPending(userId);
    }

    @Transactional
    public boolean transferOwnership(Long projectId, Long newOwnerId){

        // Project owner change

        Optional<Project> projectOptional = projectService.getProjectObjectById(projectId);
        if (projectOptional.isEmpty()) { return false; }

        Project project = projectOptional.get();
        User projectOwner = project.getCreator();

        Optional<User> newProjectOwnerOptional = userService.findUserById(newOwnerId);
        if (newProjectOwnerOptional.isEmpty()) { return false; }

        User newProjectOwner = newProjectOwnerOptional.get();
        project.setCreator(newProjectOwner);
        projectRepository.update(project);

        // set owner role for participant

        Optional<Participant> newOwnerOptional = getParticipantByUserAndProject(newOwnerId, projectId);
        if (newOwnerOptional.isEmpty()) { return false; }
        Participant newOwner = newOwnerOptional.get();

        newOwner.setParticipantRole(ParticipantRole.OWNER);
        participantRepository.update(newOwner);

        // set participant role for old owner

        Optional<Participant> oldOwnerOptional = getParticipantByUserAndProject(projectOwner.getId(), projectId);
        if (oldOwnerOptional.isEmpty()) { return false; }
        Participant oldOwner = oldOwnerOptional.get();

        oldOwner.setParticipantRole(ParticipantRole.PARTICIPANT);
        participantRepository.update(oldOwner);
        return true;
    }
}
