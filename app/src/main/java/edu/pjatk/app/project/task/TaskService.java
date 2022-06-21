package edu.pjatk.app.project.task;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantRole;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.project.team.Team;
import edu.pjatk.app.project.team.TeamService;
import edu.pjatk.app.request.TaskRequest;
import org.springframework.context.annotation.Lazy;
import edu.pjatk.app.response.TaskResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ParticipantService participantService;
    private final TeamService teamService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, ProjectRepository projectRepository,
                       ParticipantService participantService, @Lazy TeamService teamService){
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.participantService = participantService;
        this.teamService = teamService;
    }



    public Set<TaskResponse> getAllProjectTasks(Long projectId){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Project> project = projectRepository.getProjectById(projectId);

        if (loggedUser.isPresent() && project.isPresent()){

            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), projectId
            );

            if (loggedParticipant.isPresent()) {

                Optional<List<Task>> tasks = taskRepository.getAllByProjectId(projectId);

                if (tasks.isPresent() && tasks.get().size() > 0){

                    Set<TaskResponse> taskResponses = new HashSet<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String profilePhoto, username, teamName;
                    Long participantId, teamId;

                    for (Task task : tasks.get()){

                        // task for team
                        if (task.getParticipant() == null) {
                            taskResponses.add(
                                    new TaskResponse(
                                            task.getId(), task.getName(), task.getDescription(), task.getStatus().toString(),
                                            task.getCreationDate().format(formatter), task.getExpirationDate().format(formatter),
                                            null, null, null, task.getTeam().getId(), task.getTeam().getName()
                                    )
                            );
                        }
                        else {
                            // profile photo
                            if (task.getParticipant().getUser().getProfile().getPhoto() != null) {
                                profilePhoto = task.getParticipant().getUser().getProfile().getPhoto().getFileName();
                            } else {
                                profilePhoto = null;
                            }

                            // username
                            if (task.getParticipant().getUser() != null) {
                                username = task.getParticipant().getUser().getUsername();
                            } else {
                                username = null;
                            }

                            // team name
                            if (task.getTeam() != null) {
                                teamName = task.getTeam().getName();
                            } else {
                                teamName = null;
                            }

                            // participant id
                            if (task.getParticipant() != null) {
                                participantId = task.getParticipant().getId();
                            } else {
                                participantId = null;
                            }

                            // team id
                            if (task.getTeam() != null) {
                                teamId = task.getTeam().getId();
                            } else {
                                teamId = null;
                            }

                            taskResponses.add(
                                    new TaskResponse(
                                            task.getId(), task.getName(), task.getDescription(), task.getStatus().toString(),
                                            task.getCreationDate().format(formatter), task.getExpirationDate().format(formatter),
                                            participantId, username, profilePhoto, teamId, teamName
                                    )
                            );
                        }

                    }
                    return taskResponses;
                }
                else return Collections.emptySet();
            }
            else return Collections.emptySet();
        }
        else return Collections.emptySet();
    }

    @Transactional
    public Set<TaskResponse> getAllProjectTasksForParticipant(Long projectId, Long userId) {
        Optional<List<Task>> participantTasks = taskRepository.getAllByUserIdAndProjectId(projectId, userId);
        if (participantTasks.isPresent() && participantTasks.get().size() > 0) {

            Set<TaskResponse> taskResponses = new HashSet<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Task task : participantTasks.get()) {
                taskResponses.add(
                        new TaskResponse(
                                task.getId(), task.getName(), task.getDescription(), task.getStatus().toString(),
                                task.getCreationDate().format(formatter), task.getExpirationDate().format(formatter),
                                task.getParticipant().getId(), task.getParticipant().getUser().getUsername(),
                                task.getParticipant().getUser().getProfile().getPhoto().getFileName(), null, null
                        )
                );
            }
            return taskResponses;
        }
        return Collections.emptySet();
    }

    @Transactional
    public Set<TaskResponse> getAllProjectTasksForTeam(Long projectId, Long teamId){
        Optional<List<Task>> teamTasks = taskRepository.getAllByUserIdAndTeamId(projectId, teamId);
        if (teamTasks.isPresent() && teamTasks.get().size() > 0) {

            Set<TaskResponse> taskResponses = new HashSet<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Task task : teamTasks.get()) {
                taskResponses.add(
                        new TaskResponse(
                                task.getId(), task.getName(), task.getDescription(), task.getStatus().toString(),
                                task.getCreationDate().format(formatter), task.getExpirationDate().format(formatter),
                                null, null, null, task.getTeam().getId(), task.getTeam().getName()
                        )
                );
            }
            return taskResponses;
        }
        return Collections.emptySet();
    }

    @Transactional
    public boolean addTaskForParticipant(TaskRequest taskRequest){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Project> project = projectRepository.getProjectById(taskRequest.getProjectId());

        if (loggedUser.isPresent() && project.isPresent()){

            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), taskRequest.getProjectId()
            );

            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    taskRequest.getUserId(), taskRequest.getProjectId()
            );

            if (participant.isPresent() && loggedParticipant.isPresent()
                    && project.get().getParticipants().contains(participant.get())
                    && !loggedParticipant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                taskRepository.add(
                        new Task(taskRequest.getName(), taskRequest.getDescription(),
                                LocalDateTime.parse(taskRequest.getExpirationDate().replace('T', ' '), formatter),
                                participant.get(), project.get())
                );
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean addTaskForTeam(TaskRequest taskRequest){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Project> project = projectRepository.getProjectById(taskRequest.getProjectId());

        if (loggedUser.isPresent() && project.isPresent()){

            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), taskRequest.getProjectId()
            );

            Optional<Team> team = teamService.getTeam(taskRequest.getTeamId());

            if (team.isPresent() && loggedParticipant.isPresent() && team.get().getProject().equals(project.get())
                    && !loggedParticipant.get().getParticipantRole().equals(ParticipantRole.PARTICIPANT)){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                taskRepository.add(
                        new Task(taskRequest.getName(), taskRequest.getDescription(),
                                LocalDateTime.parse(taskRequest.getExpirationDate().replace('T', ' '), formatter),
                                team.get(), project.get())
                );
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean editTask(TaskRequest taskRequest, Long taskId){
        Optional<Task> task = taskRepository.getById(taskId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        if (task.isPresent() && loggedUser.isPresent()){
            Optional<Project> project = projectRepository.getProjectById(task.get().getProject().getId());
            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), task.get().getProject().getId()
            );

            if (project.isPresent() && loggedParticipant.isPresent()
                    && !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                task.get().setName(taskRequest.getName());
                task.get().setDescription(taskRequest.getDescription());
                task.get().setExpirationDate(
                        LocalDateTime.parse(taskRequest.getExpirationDate().replace('T', ' '), formatter));
                taskRepository.update(task.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean removeTask(Long taskId){
        Optional<Task> task = taskRepository.getById(taskId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (task.isPresent() && loggedUser.isPresent()){
            Optional<Project> project = projectRepository.getProjectById(task.get().getProject().getId());
            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), task.get().getProject().getId()
            );
            if (project.isPresent() && loggedParticipant.isPresent()
                    && !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                taskRepository.remove(task.get());
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Transactional
    public boolean setTaskStatusToDo(Long taskId){
        Optional<Task> task = taskRepository.getById(taskId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (task.isPresent() && loggedUser.isPresent()){

            Optional<Project> project = projectRepository.getProjectById(task.get().getProject().getId());
            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), task.get().getProject().getId()
            );

            if (task.get().getParticipant() != null){

                if (task.get().getParticipant().equals(loggedParticipant.get())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.TODO);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;

            } else if (task.get().getTeam() != null){

                if (loggedParticipant.get().getTeams().contains(task.get().getTeam())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.TODO);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    @Transactional
    public boolean setTaskStatusInProgress(Long taskId){
        Optional<Task> task = taskRepository.getById(taskId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (task.isPresent() && loggedUser.isPresent()){

            Optional<Project> project = projectRepository.getProjectById(task.get().getProject().getId());
            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), task.get().getProject().getId()
            );

            if (project.isPresent() && task.get().getParticipant() != null){

                if (task.get().getParticipant().equals(loggedParticipant.get())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.IN_PROGRESS);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;

            } else if (project.isPresent() && task.get().getTeam() != null){
                if (loggedParticipant.get().getTeams().contains(task.get().getTeam())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.IN_PROGRESS);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    @Transactional
    public boolean setTaskStatusDone(Long taskId){
        Optional<Task> task = taskRepository.getById(taskId);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (task.isPresent() && loggedUser.isPresent()){

            Optional<Project> project = projectRepository.getProjectById(task.get().getProject().getId());
            Optional<Participant> loggedParticipant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), task.get().getProject().getId()
            );

            if (task.get().getParticipant() != null){

                if (task.get().getParticipant().equals(loggedParticipant.get())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.DONE);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;

            } else if (task.get().getTeam() != null){

                if (loggedParticipant.get().getTeams().contains(task.get().getTeam())
                        || !loggedParticipant.get().getParticipantRole().equals("PARTICIPANT")){
                    task.get().setStatus(TaskStatus.DONE);
                    taskRepository.update(task.get());
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

}
