package edu.pjatk.app.project;

import edu.pjatk.app.file.File;
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
import edu.pjatk.app.response.project.*;
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

    public Set<MiniProjectResponse> getProjectsByCreatorId(Long creator_id) {

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<User> projectCreator = userService.findUserById(creator_id);

        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projects = projectRepository.getAllCreatorProjects(creator_id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        if (projectCreator.isPresent() && projects.isPresent() && !projects.get().isEmpty()){
            String projectPhoto, authorPhoto;

            for (Project p : projects.get()){
                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                if (!p.getProject_access().equals(ProjectAccess.PRIVATE) || (loggedUser.isPresent()
                        && loggedUser.get().equals(projectCreator.get()))) {
                    projectResponses.add(
                            new MiniProjectResponse(
                                    p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                    categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                    p.getCreator().getUsername(), authorPhoto
                            )
                    );
                }
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
            Set<String> projectCategories = new HashSet<>();
            loggedUser.get().getProfile().getCategories().stream().forEach(category -> userCategories.add(category.getTitle()));

            for (Project p : allProjects.get()){

                p.getCategories().stream().forEach(category -> projectCategories.add(category.getTitle()));

                if (projectCategories.stream().filter(s -> userCategories.contains(s)).collect(Collectors.toSet()).size() > 0){

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

    public List<FullProjectResponse> getAllNonPrivateProjects(){

        Optional<List<Project>> allProjects = projectRepository.getAllNonPrivateProjects();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<FullProjectResponse> responseList = new ArrayList<>();

        if (allProjects.isPresent() && allProjects.get().size() > 0){

            for (Project project : allProjects.get()){

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

                responseList.add(new FullProjectResponse(
                        project.getId(), projectPhoto,
                        project.getProject_name(), project.getProject_introduction(),
                        project.getProject_description(), project.getCreation_date().format(formatter),
                        project.getProject_status().name(), project.getProject_access().name(), categories,
                        ytLink, gitLink, fbLink, kickLink,
                        project.getCreator().getId(), project.getCreator().getUsername(),
                        authorPhoto, averageRating, numberOfVotes, participants
                ));

            }
            return responseList;
        }
        else return Collections.emptyList();
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

    //If loggedUser is project owner or loggedUser is project's participant with MODERATOR ROLE
    @Transactional
    public void editProject(ProjectRequest projectRequest, MultipartFile photo, Long id){

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
            project.setId(id);
            projectRepository.update(project);
        }
    }


    //Rating section
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

    public List<FullProjectResponse> getTopRatedProjects(){

        List<FullProjectResponse> allProjects = this.getAllNonPrivateProjects();

        if (allProjects.isEmpty()) return Collections.emptyList();
        else {
            allProjects.sort(Comparator.comparing(FullProjectResponse::getNumberOfVotes).reversed());

            List<FullProjectResponse> top10 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if(allProjects.size() > i) top10.add(allProjects.get(i));
            }

            top10.sort(Comparator.comparing(FullProjectResponse::getAverageRating).reversed());

            return top10;
        }
    }

    public List<FullProjectResponse> getRandomRecommendedProjects(){

        Optional<List<Project>> allProjects = projectRepository.getRandomRecommendedProjects10();
        if (allProjects.isEmpty()) return Collections.emptyList();

        List<Project> random10 = allProjects.get();
        List<FullProjectResponse> random10Response = new ArrayList<>();

        for (Project project: random10) {
            //Return average rating if there is more than one vote
            float averageRating = (project.getRatings().size() > 0 ?
                    ((Integer) project.getRatings().stream().mapToInt(Rating::getValue).sum()).floatValue()/project.getRatings().size()
                    : 0);
            int numberOfVotes = project.getRatings().size();

            //Return IDs of project members
            Set<Long> participants = new HashSet<>();
            for (Participant p : project.getParticipants()){
                if (!p.isPending()) participants.add(p.getUser().getId());
            }

            Set<String> categories = new HashSet<>();
            if (!project.getCategories().isEmpty()) {
                for (Category c : project.getCategories()){
                    categories.add(c.getTitle());
                }
            }

            String projectPhoto = "";
            if (project.getPhoto() != null) {
                projectPhoto = project.getPhoto().toString();
            }

            String userPhoto = "";
            if (project.getCreator().getProfile().getPhoto() != null) {
                userPhoto = project.getCreator().getProfile().getPhoto().toString();
            }

            FullProjectResponse projectResponse = new FullProjectResponse(
                    project.getId(), projectPhoto, project.getProject_name(), project.getProject_introduction(),
                    project.getProject_description(), project.getCreation_date().toString(), project.getProject_status().toString(),
                    project.getProject_access().toString(),categories, project.getYoutube_link(), project.getGithub_link(),
                    project.getFacebook_link(), project.getKickstarter_link(), project.getCreator().getId(),
                    project.getCreator().getUsername(), userPhoto,
                    averageRating, numberOfVotes, participants
            );
            System.out.println();
            random10Response.add(projectResponse);
        }
        return random10Response;
    }


    //Project files section
    public Set<FileResponse> getProjectFiles(Long projectId){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Optional<Project> project = projectRepository.getProjectById(projectId);
        if (project.isEmpty()) return Collections.emptySet();

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                loggedUser.get().getId(), project.get().getId());

        if (participant.isPresent()){

            Set<FileResponse> fileResponses = new HashSet<>();
            String profilePhoto;

            for (File f : project.get().getFiles()){
                try {
                    profilePhoto = f.getUser().getProfile().getPhoto().getFileName();
                } catch (NullPointerException e) { profilePhoto = null; }
                fileResponses.add(new FileResponse(f.getId(), f.getName(), f.getUrl(), f.getUser().getId(),
                        f.getUser().getUsername(), profilePhoto, f.getSize(),
                        f.getUploadDate().format(formatter)));
            }
            return fileResponses;
        }
        else return Collections.emptySet();
    }


    //Project roles section
    @Transactional
    public boolean promoteMember(Long userId, Long projectId){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(loggedUser.get().getId(), projectId);
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<Participant> participant = participantService.getParticipantByUserAndProject(userId, projectId);

        if (loggedUser.isPresent() && loggedParticipant.isPresent() && participant.isPresent() &&
                project.isPresent() && participant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT) &&
                project.get().getCreator().equals(loggedUser.get())){
            participant.get().setParticipantRole(ParticipantRole.MODERATOR);
            participantService.updateParticipant(participant.get());
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean degradeMember(Long userId, Long projectId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(loggedUser.get().getId(), projectId);
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<Participant> participant = participantService.getParticipantByUserAndProject(userId, projectId);

        if (loggedUser.isPresent() && loggedParticipant.isPresent() && project.isPresent() &&
                participant.isPresent() && participant.get().getParticipantRole().equals(ParticipantRole.MODERATOR) &&
                project.get().getCreator().equals(loggedUser.get())){
            participant.get().setParticipantRole(ParticipantRole.PARTICIPANT);
            participantService.updateParticipant(participant.get());
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean kickMember(Long userId, Long projectId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(loggedUser.get().getId(), projectId);
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<Participant> participant = participantService.getParticipantByUserAndProject(userId, projectId);

        if (loggedUser.isPresent() && loggedParticipant.isPresent() && project.isPresent() &&
                participant.isPresent() && !loggedUser.get().getId().equals(userId) &&
                !loggedParticipant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT) &&
                !project.get().getCreator().getId().equals(userId)){
            if (loggedParticipant.get().getParticipantRole().equals(ParticipantRole.OWNER)) {
                participantService.removeParticipant(participant.get());
                return true;
            }
            else if (loggedParticipant.get().getParticipantRole().equals(ParticipantRole.MODERATOR) && participant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)) {
                participantService.removeParticipant(participant.get());
                return true;
            }
            else return false;
        }
        else return false;
    }


    //Membership and invitations section
    public Set<ProjectJoinRequestResponse> getAllPendingRequests(){
        //Requests to join 'protected' project
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<Participant>> participants = participantService.getAllPending(loggedUser.get().getId());

        if (participants.isPresent() && participants.get().size() > 0){
            String profilePhoto;
            Set<ProjectJoinRequestResponse> pending = new HashSet<>();

            for (Participant p : participants.get()){
                try {
                    profilePhoto =  p.getUser().getProfile().getPhoto().getFileName();
                } catch (NullPointerException e) { profilePhoto = null;  }
                pending.add(new ProjectJoinRequestResponse(
                        p.getId(), p.getUser().getId(), p.getUser().getUsername(),
                        profilePhoto, p.getProject().getId(),
                        p.getProject().getProject_name()
                ));
            }
            return pending;
        }
        else return Collections.emptySet();
    }

    public Set<InvitationResponse> getAllInvitations(){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<ProjectInvitation>> invitations = projectInvitationService.getAllInvitationsByUserId(loggedUser.get().getId());
        Set<InvitationResponse> invitationResponses = new HashSet<>();

        if (invitations.isPresent() && invitations.get().size() > 0){
            String projectPhoto;
            for (ProjectInvitation invitation : invitations.get()){
                try {
                    projectPhoto =  invitation.getProject().getPhoto().getFileName();
                } catch (NullPointerException e) { projectPhoto = null;  }
                invitationResponses.add(
                        new InvitationResponse(invitation.getId(), invitation.getProject().getId(),
                                projectPhoto, invitation.getProject().getProject_name())
                );
            }
            return invitationResponses;
        }
        else return Collections.emptySet();
    }

    public Set<MiniProjectResponse> getAllProjectsWhereUserJoined(){
        //Includes only members with pending = false
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<Participant>> participantOf = participantService.getAllWhereUserJoined(loggedUser.get().getId());

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

    public Set<MemberResponse> getProjectMembers(Long projectId){

        Optional<Project> project = projectRepository.getProjectById(projectId);

        if (project.isPresent()){

            String profilePhoto;
            Set<MemberResponse> members = new HashSet<>();

            for (Participant participant : project.get().getParticipants()){
                if (!participant.isPending()){
                    try { profilePhoto = participant.getUser().getProfile().getPhoto().getFileName(); }
                    catch (NullPointerException e) { profilePhoto = null; }
                    members.add(new MemberResponse(participant.getUser().getId(), participant.getUser().getUsername(),
                            profilePhoto, participant.getParticipantRole().toString())
                    );
                }
            }
            return members;
        }
        else return Collections.emptySet();
    }

    @Transactional
    public boolean inviteToProject(Long projectId, Long userId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<User> userToInvite = userService.findUserById(userId);
        Optional<Project> project = projectRepository.getProjectById(projectId);
        Optional<ProjectInvitation> invitation = projectInvitationService.getInvitationByUserIdAndProjectId(userId, projectId);

        boolean userIsProjectModerator = !new HashSet<>(project.get().getParticipants()).stream().filter(
                participant -> loggedUser.get().getParticipants().contains(participant) && participant.getParticipantRole().equals(ParticipantRole.MODERATOR)
        ).collect(Collectors.toSet()).isEmpty();

        boolean userToInviteIsNotAlreadyProjectParticipant = new HashSet<>(project.get().getParticipants()).stream().filter(
                participant -> userToInvite.get().getParticipants().contains(participant)).collect(Collectors.toSet()).isEmpty();


        if ( userToInvite.isPresent() && project.isPresent() && invitation.isEmpty() &&
                (project.get().getCreator().equals(loggedUser.get()) || userIsProjectModerator)
                && userToInviteIsNotAlreadyProjectParticipant){
            projectInvitationService.addProjectInvitation(new ProjectInvitation(project.get(), userToInvite.get()));
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean acceptInvitation(Long invitationId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<ProjectInvitation> invitation = projectInvitationService.getInvitationById(invitationId);

        if (invitation.isPresent() && invitation.get().getReceiver().equals(loggedUser.get())){
            Optional<Project> project = projectRepository.getProjectById(invitation.get().getProject().getId());
            project.get().getParticipants().add(new Participant(loggedUser.get(), project.get(), false, ParticipantRole.PARTICIPANT));
            projectRepository.update(project.get());
            projectInvitationService.removeProjectInvitation(invitation.get());
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean rejectInvitation(Long invitationId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<ProjectInvitation> invitation = projectInvitationService.getInvitationById(invitationId);

        if (invitation.isPresent() && invitation.get().getReceiver().equals(loggedUser.get())){
            projectInvitationService.removeProjectInvitation(invitation.get());
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean joinProject(Long id){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Project> project = projectRepository.getProjectById(id);

        Set<Participant> participants = new HashSet<>(project.get().getParticipants());
        participants.retainAll(loggedUser.get().getParticipants());
        System.out.println();
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

    @Transactional
    public void acceptPending(Long pendingId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Participant> participant = participantService.getById(pendingId);

        if(participant.isPresent() && participant.get().getProject().getCreator().equals(loggedUser.get())){
            participant.get().setPending(false);
            participantService.updateParticipant(participant.get());
        }
    }

    @Transactional
    public void rejectPending(Long pendingId){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Participant> participant = participantService.getById(pendingId);

        if(participant.isPresent() && participant.get().getProject().getCreator().equals(loggedUser.get())){
            participantService.removeParticipant(participant.get());
        }
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
