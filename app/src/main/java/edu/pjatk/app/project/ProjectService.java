package edu.pjatk.app.project;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.project.category.CategoryService;
import edu.pjatk.app.project.invitation.ProjectInvitation;
import edu.pjatk.app.project.invitation.ProjectInvitationService;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantRole;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.request.ProjectRequest;
import edu.pjatk.app.response.project.FullProjectResponse;
import edu.pjatk.app.response.project.MiniProjectResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PhotoService photoService;
    private final ProjectInvitationService projectInvitationService;
    private final ParticipantService participantService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CategoryService categoryService,
                          UserService userService, PhotoService photoService,
                          ProjectInvitationService projectInvitationService,
                          ParticipantService participantService) {
        this.projectRepository = projectRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.photoService = photoService;
        this.projectInvitationService = projectInvitationService;
        this.participantService = participantService;
    }


    public Optional<FullProjectResponse> getProjectById(Long id) {

        Optional<Project> projectOptional = projectRepository.getProjectById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projectOptional.isPresent()){
            Project project = projectOptional.get();
            String projectPhoto, authorPhoto, ytLink, gitLink, fbLink, kickLink;
            Set<String> categories = new HashSet<>();

            try { projectPhoto = project.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
            try { authorPhoto = project.getCreator().getProfile().getPhoto().getFileName(); }
            catch (NullPointerException e) { authorPhoto = null;}
            try { ytLink = project.getYoutube_link(); } catch (NullPointerException e) { ytLink = null;}
            try { gitLink = project.getGithub_link(); } catch (NullPointerException e) { gitLink = null;}
            try { fbLink = project.getFacebook_link(); } catch (NullPointerException e) { fbLink = null;}
            try { kickLink = project.getKickstarter_link(); } catch (NullPointerException e) { kickLink = null;}

            for (Category c : project.getCategories()){
                categories.add(c.getTitle());
            }

            //Return average rating if there is more than one vote
            float averageRating = (project.getRatings().size() > 0 ?
                    project.getRatings().stream().collect(Collectors.summingInt(Rating::getValue)).floatValue()/project.getRatings().size()
                    : 0);
            int numberOfVotes = project.getRatings().size();

            //Return IDs of project members
            Set<Long> participants = new HashSet<>();
            for (Participant p : project.getParticipants()){
                if (!p.isPending()) participants.add(p.getUser().getId());
            }

            return Optional.of(new FullProjectResponse(
                    project.getId(), projectPhoto,
                    project.getProject_name(), project.getProject_introduction(),
                    project.getProject_description(), project.getCreation_date().format(formatter),
                    project.getProject_status().name(), project.getProject_access().name(), categories,
                    ytLink, gitLink, fbLink, kickLink,
                    project.getCreator().getId(), project.getCreator().getUsername(),
                    authorPhoto, averageRating, numberOfVotes, participants
            ));
        }
        else return Optional.empty();
    }

    public Set<MiniProjectResponse> getProjectByName(String project_name) {

        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projectList = projectRepository.getProjectsByTitle(project_name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projectList.isPresent() && !projectList.get().isEmpty()){

            String projectPhoto, authorPhoto;

            for (Project p : projectList.get()){

                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                p.getCreator().getUsername(), authorPhoto
                        )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    public Set<MiniProjectResponse> getProjectByCategory(String categoryTitle){
        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projectList = projectRepository.getByCategory(categoryTitle);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projectList.isPresent() && !projectList.get().isEmpty()){

            String projectPhoto, authorPhoto;

            for (Project p : projectList.get()){

                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                p.getCreator().getUsername(), authorPhoto
                        )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    public Set<MiniProjectResponse> getAllProjects(Long creator_id) {

        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projects = projectRepository.getAllProjects(creator_id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projects.isPresent() && !projects.get().isEmpty()){
            String projectPhoto, authorPhoto;

            for (Project p : projects.get()){
                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                p.getCreator().getUsername(), authorPhoto
                                )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    public Set<MiniProjectResponse> getAllProposedProjects(){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<List<Project>> allProjects = projectRepository.getAllProposedProjects(loggedUser.get().getId());


        if (loggedUser.isPresent() && !allProjects.get().isEmpty()){

            Set<MiniProjectResponse> proposedProjects = new HashSet<>();
            Set<String> userCategories = new HashSet<>();
            loggedUser.get().getProfile().getCategories().stream().forEach(category -> userCategories.add(category.getTitle()));
            Set<String> projectCategories = new HashSet<>();

            for (Project p : allProjects.get()){

                p.getCategories().stream().forEach(category -> projectCategories.add(category.getTitle()));

                if (projectCategories.retainAll(userCategories) && !projectCategories.isEmpty()){

                    String projectPhoto, authorPhoto;
                    Set<String> categories = new HashSet<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                    try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                    catch (NullPointerException e) { authorPhoto = null;}

                    for (Category c : p.getCategories()){
                        categories.add(c.getTitle());
                    }

                    proposedProjects.add(
                            new MiniProjectResponse(
                                    p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                    categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                    p.getCreator().getUsername(), authorPhoto
                            )
                    );

                    projectCategories.clear();
                }
                projectCategories.clear();
            }
            return proposedProjects;
        }
        else return Collections.emptySet();
    }



    public Set<MiniProjectResponse> getAllProjectsWhereUserIsMember(){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<Participant>> participantOf = participantService.getAllWhereUserIsMember(loggedUser.get().getId());

        if (participantOf.isPresent() && participantOf.get().size() > 0){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Set<MiniProjectResponse> projectResponses = new HashSet<>();
            String projectPhoto, authorPhoto;

            for (Participant participant : participantOf.get()){
                Set<String> categories = new HashSet<>();
                try { projectPhoto = participant.getProject().getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = participant.getProject().getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : participant.getProject().getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                participant.getProject().getId(), projectPhoto, participant.getProject().getProject_name(),
                                participant.getProject().getProject_introduction(), categories,
                                participant.getProject().getCreation_date().format(formatter),
                                participant.getProject().getCreator().getId(),
                                participant.getProject().getCreator().getUsername(), authorPhoto
                        )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    @Transactional
    public void rateProject(Long id, int ratingValue){

        Optional<Project> project = projectRepository.getProjectById(id);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (project.isPresent() && loggedUser.isPresent()){

            Rating newRate = new Rating(ratingValue, loggedUser.get().getProfile(), project.get());

            Optional<Rating> oldRate = project.get().getRatings().stream().filter(
                    rating -> rating.getProfile().equals(loggedUser.get().getProfile())).findFirst();

            //Check if user already rated this project
            if (oldRate.isPresent()){
                for (int i=0; i < project.get().getRatings().size(); i++){
                    if (project.get().getRatings().get(i).equals(oldRate.get())){
                        project.get().getRatings().get(i).setValue(ratingValue);
                    }
                }
            } else project.get().getRatings().add(newRate);

            projectRepository.update(project.get());
        }
    }

    public int getMyRating(Long projectId){
        Optional<Project> projectOptional = projectRepository.getProjectById(projectId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (loggedUser.isPresent() && projectOptional.isPresent()){
            Project project = projectOptional.get();
            Optional<Rating> rate = project.getRatings().stream().filter(
                    rating -> rating.getProfile().equals(loggedUser.get().getProfile())).findFirst();
            if (rate.isPresent()) return rate.get().getValue();
            else return 0;
        }
        else return 0;
    }

    @Transactional
    public void createProject(ProjectRequest projectRequest, MultipartFile photo){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Photo projectPhoto = (photo != null ? photoService.uploadPhoto(photo) : null);

        if (loggedUser.isPresent())
        {
            Set<Category> categories = new HashSet<>();
            for (String category : projectRequest.getCategory()){
                categories.add(categoryService.getCategoryByTitle(category).get());
            }

            Project project = new Project(
                    projectRequest.getTitle(), projectRequest.getIntroduction(), projectRequest.getDescription(),
                    LocalDateTime.now(), categories, ProjectStatus.OPEN, projectRequest.getAccess(),
                    projectRequest.getYoutubeLink(), projectRequest.getFacebookLink(), projectRequest.getGithubLink(),
                    projectRequest.getKickstarterLink(), loggedUser.get(), projectPhoto
            );

            //Add project to repository
            projectRepository.createProject(project);

            //Add project creator as participant with OWNER Role
            project.getParticipants().add(new Participant(loggedUser.get(), project, false, ParticipantRole.OWNER));
            projectRepository.update(project);
        }
    }

    @Transactional
    public void editProject(ProjectRequest projectRequest, MultipartFile photo){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Photo projectPhoto = (photo != null ? photoService.uploadPhoto(photo) : null);

        if (loggedUser.isPresent())
        {
            Set<Category> categories = new HashSet<>();
            for (String category : projectRequest.getCategory()){
                categories.add(categoryService.getCategoryByTitle(category).get());
            }

            Project project = new Project(
                    projectRequest.getTitle(), projectRequest.getIntroduction(), projectRequest.getDescription(),
                    LocalDateTime.now(), categories, ProjectStatus.OPEN, projectRequest.getAccess(),
                    projectRequest.getYoutubeLink(), projectRequest.getFacebookLink(), projectRequest.getGithubLink(),
                    projectRequest.getKickstarterLink(), loggedUser.get(), projectPhoto
            );
            projectRepository.update(project);
        }
    }

    @Transactional
    public void inviteToProject(Long projectId, Long userId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<User> userToInvite = userService.findUserById(userId);
        Optional<Project> project = projectRepository.getProjectById(projectId);

        if (userToInvite.isPresent() && project.isPresent() && project.get().getCreator().equals(loggedUser)){
            projectInvitationService.addProjectInvitation(new ProjectInvitation(project.get(), userToInvite.get()));
        }
    }

    @Transactional
    public void acceptInvitation(Long invitationId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<ProjectInvitation> invitation = projectInvitationService.getInvitationById(invitationId);

        if (invitation.isPresent() && invitation.get().getReceiver().equals(loggedUser)){
            Optional<Project> project = projectRepository.getProjectById(invitation.get().getProject().getId());
            project.get().getParticipants().add(new Participant(loggedUser.get(), project.get(), false, ParticipantRole.PARTICIPANT));
            projectRepository.update(project.get());
            projectInvitationService.removeProjectInvitation(invitation.get());
        }
    }

    @Transactional
    public void rejectInvitation(Long invitationId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<ProjectInvitation> invitation = projectInvitationService.getInvitationById(invitationId);

        if (invitation.isPresent() && invitation.get().getReceiver().equals(loggedUser)){
            projectInvitationService.removeProjectInvitation(invitation.get());
        }
    }

    @Transactional
    public boolean joinProject(Long id){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(id);

        Set<Participant> participants = new HashSet<>(project.get().getParticipants());
        participants.retainAll(loggedUser.get().getParticipants());

        //Check if user is not already in project
        if (loggedUser.isPresent() && participants.size() == 0){

            //If project is public
            if (project.get().getProject_access().equals(ProjectAccess.PUBLIC)){
                project.get().getParticipants().add(
                        new Participant(loggedUser.get(), project.get(), false, ParticipantRole.PARTICIPANT)
                );
            }
            else {
                //If project is protected
                project.get().getParticipants().add(
                        new Participant(loggedUser.get(), project.get(), true, ParticipantRole.PARTICIPANT)
                );
            }
            projectRepository.update(project.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public boolean leaveProject(Long id){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(id);

        Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                loggedUser.get().getId(), project.get().getId()
        );

        if (participant.isPresent()){
            project.get().getParticipants().remove(participant.get());
            projectRepository.update(project.get());
            return true;
        }
        else return false;
    }



    public void deleteProject(Long id) {
        projectRepository.deleteProject(id);
    }

    public void editProjectName(Long id, String project_name) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectName(id, project_name);
        }
    }

    public void editProjectCategory(Long id, String project_category) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectCategory(id, project_category);
        }
    }

    public void editProjectStatus(Long id, String project_status) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectStatus(id, project_status);
        }
    }
}
