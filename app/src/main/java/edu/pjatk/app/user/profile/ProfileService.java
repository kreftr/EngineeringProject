package edu.pjatk.app.user.profile;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.response.ProfileResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PhotoService photoService;
    private final UserService userService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, PhotoService photoService, UserService userService){
        this.profileRepository = profileRepository;
        this.photoService = photoService;
        this.userService = userService;
    }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Transactional
    public void updateProfile(ProfileEditRequest editRequest, MultipartFile profilePhoto){

        Profile userProfile = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).get().getProfile();

        Photo newProfilePhoto;
        if (!profilePhoto.isEmpty()) newProfilePhoto = photoService.uploadPhoto(profilePhoto);
        else newProfilePhoto = null;

        if (editRequest.getName() != null) userProfile.setName(editRequest.getName());
        if (editRequest.getSurname() != null) userProfile.setSurname(editRequest.getSurname());
        if (editRequest.getBio() != null) userProfile.setBio(editRequest.getBio());
        if (newProfilePhoto != null) {
            if (userProfile.getPhoto() != null) photoService.removePhoto(userProfile.getPhoto());
            userProfile.setPhoto(newProfilePhoto);
        }

        profileRepository.update(userProfile);
    }

    @Transactional
    public Optional<ProfileResponse> returnProfileResponse(Long user_id){
        Optional<User> user = userService.findUserById(user_id);
        if (user.isEmpty()){
            return Optional.empty();
        }
        else {
            String photoFileName;
            //TODO: find better way of handling hibernate null photo reference exception
            Profile profile = user.get().getProfile();
            try {
                photoFileName = profile.getPhoto().getFileName();
            } catch (Exception e) {
                photoFileName = null;
            }
            return Optional.of(new ProfileResponse(user.get().getUsername(), profile.getName(),
                    profile.getSurname(), profile.getBio(), photoFileName));
        }
    }

}
