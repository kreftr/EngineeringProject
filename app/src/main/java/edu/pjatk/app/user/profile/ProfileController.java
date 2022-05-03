package edu.pjatk.app.user.profile;

import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.request.ProfileEditRequest;
import edu.pjatk.app.response.profile.FullProfileResponse;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.response.profile.MiniProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin("http://localhost:3000")
public class ProfileController {

    private final ProfileService profileService;
    private final PhotoService photoService;

    @Autowired
    public ProfileController(ProfileService profileService, PhotoService photoService){
        this.profileService = profileService;
        this.photoService = photoService;
    }


    @RequestMapping(params = "id", method = RequestMethod.GET)
    public ResponseEntity getProfile(@RequestParam Long id){
        Optional<FullProfileResponse> profile = profileService.findProfileById(id);
        if (profile.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Profile not found"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity(profile.get(),HttpStatus.OK);
        }
    }

    @RequestMapping(params = "username", method = RequestMethod.GET)
    public ResponseEntity getProfile(@RequestParam String username){
        Optional<List<MiniProfileResponse>> profiles = profileService.findProfilesByUsername(username);
        if(username.isBlank()) {
            username = " ";
            return new ResponseEntity(
                    new ResponseMessage("No matches for '"+username+"'"), HttpStatus.NOT_FOUND
            );
        }
        else if (profiles.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("No matches for '"+username+"'"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity(profiles.get(), HttpStatus.OK);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity editProfile(@Valid @RequestPart ProfileEditRequest editRequest,
                                      @RequestPart(required = false) MultipartFile profilePhoto){
        if (profilePhoto == null){
            profileService.updateProfile(editRequest, null);
            return new ResponseEntity(HttpStatus.OK);
        }
        else if (photoService.isPhoto(profilePhoto)){
            profileService.updateProfile(editRequest, profilePhoto);
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            return new ResponseEntity(Map.of("error", "Unsupported photo format"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/pagination")
    public ResponseEntity getProfile(@RequestParam String username, @RequestParam int pageNumber,
                                     @RequestParam int pageSize) {
        Optional<List<MiniProfileResponse>> profiles = profileService.findProfilesWithPagination(username, pageNumber, pageSize);
        if (profiles.isEmpty()) {
            return new ResponseEntity(
                new ResponseMessage("No matches for '"+username+"'"), HttpStatus.NOT_FOUND
            );
        } else {
            return new ResponseEntity(profiles.get(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/number")
    public ResponseEntity getProfilesNumber(@RequestParam String username) {
        return new ResponseEntity(profileService.getProfilesNumberByUsername(username), HttpStatus.OK);
    }
}
