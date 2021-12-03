package edu.pjatk.app.user.profile;

import edu.pjatk.app.response.profile.FullProfileResponse;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.response.profile.MiniProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }


    @CrossOrigin("http://localhost:3000")
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

    @CrossOrigin("http://localhost:3000")
    @RequestMapping(params = "username", method = RequestMethod.GET)
    public ResponseEntity getProfile(@RequestParam String username){
        Optional<List<MiniProfileResponse>> profiles = profileService.findProfilesByUsername(username);
        if (profiles.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("No matches for '"+username+"'"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity(profiles.get(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity editProfile(@RequestPart(required = false) ProfileEditRequest editRequest,
                                      @RequestPart(required = false) MultipartFile profilePhoto){

        profileService.updateProfile(editRequest, profilePhoto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
