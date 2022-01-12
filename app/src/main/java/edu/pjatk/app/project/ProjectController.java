package edu.pjatk.app.project;


import edu.pjatk.app.request.ProjectRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.response.project.FullProjectResponse;
import edu.pjatk.app.response.project.InvitationResponse;
import edu.pjatk.app.response.project.MiniProjectResponse;
import edu.pjatk.app.response.project.ProjectJoinRequestResponse;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/project")

public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    private ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping(value = "/getProjectById/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {

        Optional<FullProjectResponse> projectResponse = projectService.getProjectById(id);

        if (projectResponse.isPresent())
        {
            return new ResponseEntity<>(
                    projectResponse.get(), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getProjectByName/{project_name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String project_name) {

        Set<MiniProjectResponse> projects = projectService.getProjectByName(project_name);

        if (projects.isEmpty())
        {
            return new ResponseEntity<>(
                    new ResponseMessage("No matches for '"+project_name+"'"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity<>(
                    projects, HttpStatus.OK
            );
        }
    }

    @GetMapping(value = "/getProjectByCategory/{title}")
    public ResponseEntity getProjectByCategory(@PathVariable String title){
        Set<MiniProjectResponse> projects = projectService.getProjectByCategory(title);
        if (projects.isEmpty()){
            return new ResponseEntity<>(
                    new ResponseMessage("No matches for "+title+" category"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity<>(
                    projects, HttpStatus.OK
            );
        }
    }

    @GetMapping(value = "/getAllProjects/{creator_id}")
    public ResponseEntity<?> getAllCreatorProjects(@PathVariable Long creator_id) {
        Set<MiniProjectResponse> projects = projectService.getAllCreatorProjects(creator_id);

        if (!projects.isEmpty())
        {
            return new ResponseEntity<>(
                    projects, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value= "/getProposed")
    public ResponseEntity getProposedProjects(){
        Set<MiniProjectResponse> proposedProjects = projectService.getAllProposedProjects();
        if (proposedProjects.isEmpty()){
            return new ResponseEntity<>(
                    new ResponseMessage("We couldn't find any proper project for you :("), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity(proposedProjects, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/rateProject/{id}", params = "rating")
    public ResponseEntity rateProject(@PathVariable Long id, @RequestParam int rating){

        if (rating > 0 && rating <= 5){
            projectService.rateProject(id, rating);
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getMyRating/{projectId}")
    public ResponseEntity getMyRating(@PathVariable Long projectId){
        return new ResponseEntity(projectService.getMyRating(projectId), HttpStatus.OK);
    }

    @GetMapping(value = "/ranking")
    public ResponseEntity getTopOfAllTime(){
        List<FullProjectResponse> best = projectService.getBestOfAll();
        if(!best.isEmpty()){
            return new ResponseEntity(best, HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }



    @PostMapping(value = "/createProject", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createProject(@Valid @RequestPart ProjectRequest projectRequest,
                                           @RequestPart(required = false) MultipartFile projectPhoto) {

        projectService.createProject(projectRequest, projectPhoto);
        return new ResponseEntity<>(new ResponseMessage("Project created!"), HttpStatus.OK);
    }

    @PostMapping(value = "/editProject/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> editProject(@RequestPart ProjectRequest projectRequest,
                                         @RequestPart(required = false) MultipartFile projectPhoto,
                                         @PathVariable Long id) {

        projectService.editProject(projectRequest, projectPhoto, id);
        return new ResponseEntity<>(new ResponseMessage("Project edited!"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllProjectsWhereIsMember")
    public ResponseEntity getAllProjectsWhereIsMember(){
        Set<MiniProjectResponse> projects = projectService.getAllProjectsWhereUserIsMember();
        if (!projects.isEmpty()){
            return new ResponseEntity(projects, HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAllInvitations")
    public ResponseEntity getAllInvitations(){
        Set<InvitationResponse> invitations = projectService.getAllInvitations();
        if (!invitations.isEmpty()){
            return new ResponseEntity(invitations,HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/join/{id}")
    public ResponseEntity joinProject(@PathVariable Long id){

        Optional<FullProjectResponse> project = projectService.getProjectById(id);

        if (project.isPresent()){
            if (project.get().getStatus().equals(ProjectStatus.OPEN.toString())) {

                if (project.get().getAccess().equals(ProjectAccess.PUBLIC.toString()) ||
                        project.get().getAccess().equals(ProjectAccess.PROTECTED.toString())) {
                    if(projectService.joinProject(id)) return new ResponseEntity(HttpStatus.OK);
                    else return new ResponseEntity(new ResponseMessage("You are already a participant in this project"), HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity(new ResponseMessage("You can't join unless project's owner invites you") ,HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity(
                        new ResponseMessage("This project is bo longer open"), HttpStatus.UNAUTHORIZED
                );
            }
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("There is no project with this id"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/leave/{id}")
    public ResponseEntity leaveProject(@PathVariable Long id){

        Optional<FullProjectResponse> project = projectService.getProjectById(id);

        if (project.isPresent()){
            if(projectService.leaveProject(id)) return new ResponseEntity(HttpStatus.OK);
            else return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("There is no project with this id"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/pendingRequests")
    public ResponseEntity getPendingRequests(){
        Set<ProjectJoinRequestResponse> pending = projectService.getAllPendingRequests();
        if (!pending.isEmpty()){
            return new ResponseEntity(pending, HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/acceptPending/{pendingId}")
    public ResponseEntity acceptPending(@PathVariable Long pendingId){
        projectService.acceptPending(pendingId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/rejectPending/{pendingId}")
    public ResponseEntity rejectPending(@PathVariable Long pendingId){
        projectService.rejectPending(pendingId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/inviteToProject/{projectId}", params = "userId")
    public ResponseEntity inviteToProject(@PathVariable Long projectId, @RequestParam Long userId){
        if (projectService.inviteToProject(projectId, userId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/acceptInvitation/{invitationId}")
    public ResponseEntity acceptInvitation(@PathVariable Long invitationId){
        if (projectService.acceptInvitation(invitationId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/rejectInvitation/{invitationId}")
    public ResponseEntity rejectInvitation(@PathVariable Long invitationId){
        if (projectService.rejectInvitation(invitationId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }



    @DeleteMapping(value = "/deleteProject/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(
                new ResponseMessage("Project deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectName/{id}/{project_name}")
    public ResponseEntity<?> editProjectName(@PathVariable long id, @PathVariable String project_name) {
        projectService.editProjectName(id, project_name);
        return new ResponseEntity<>(
                new ResponseMessage("Project name change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectCategory/{id}/{project_category}")
    public ResponseEntity<?> editProjectCategory(@PathVariable long id, @PathVariable String project_category) {
        projectService.editProjectCategory(id, project_category);
        return new ResponseEntity<>(
                new ResponseMessage("Project category change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectStatus/{id}/{project_status}")
    public ResponseEntity<?> editProjectStatus(@PathVariable long id, @PathVariable String project_status) {
        projectService.editProjectStatus(id, project_status);
        return new ResponseEntity<>(
                new ResponseMessage("Project status change is complete"), HttpStatus.OK
        );
    }

}
