package edu.pjatk.app.user.profile;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }


    @GetMapping
    public ResponseEntity getProfile(@RequestParam Long id){
        Optional<Profile> profile = profileService.findProfileById(id);
        if (profile.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Profile not found"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity(profile.get(),HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity editProfile(@RequestPart(required = false) ProfileEditRequest editRequest,
                                      @RequestPart(required = false) MultipartFile profilePhoto){

        profileService.updateProfile(editRequest, profilePhoto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
