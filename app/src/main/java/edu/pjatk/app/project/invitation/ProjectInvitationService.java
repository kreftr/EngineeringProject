package edu.pjatk.app.project.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectInvitationService {

    private final ProjectInvitationRepository projectInvitationRepository;

    @Autowired
    public ProjectInvitationService(ProjectInvitationRepository projectInvitationRepository){
        this.projectInvitationRepository = projectInvitationRepository;
    }

    public Optional<ProjectInvitation> getInvitationById(Long id){
        return projectInvitationRepository.getById(id);
    }

    public Optional<List<ProjectInvitation>> getAllInvitationsByUserId(Long id){
        return projectInvitationRepository.getByUserId(id);
    }

    public Optional<ProjectInvitation> getInvitationByUserIdAndProjectId(Long userId, Long projectId){
        return projectInvitationRepository.getByUserIdAndProjectId(userId,  projectId);
    }

    public void addProjectInvitation(ProjectInvitation projectInvitation){
        projectInvitationRepository.add(projectInvitation);
    }

    public void removeProjectInvitation(ProjectInvitation projectInvitation){
        projectInvitationRepository.remove(projectInvitation);
    }

}
