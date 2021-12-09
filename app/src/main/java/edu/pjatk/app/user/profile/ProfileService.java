package edu.pjatk.app.user.profile;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.request.ProfileEditRequest;
import edu.pjatk.app.response.profile.FullProfileResponse;
import edu.pjatk.app.response.profile.MiniProfileResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

        userProfile.setName(editRequest.getName());
        userProfile.setSurname(editRequest.getSurname());
        userProfile.setBio(editRequest.getBio());

        if (profilePhoto != null){
            Photo newProfilePhoto = photoService.uploadPhoto(profilePhoto);
            if (newProfilePhoto != null) {
                if (userProfile.getPhoto() != null) photoService.removePhoto(userProfile.getPhoto());
                userProfile.setPhoto(newProfilePhoto);
            }
        }

        profileRepository.update(userProfile);
    }

    public Optional<FullProfileResponse> findProfileById(Long user_id){
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
            return Optional.of(new FullProfileResponse(user.get().getUsername(), profile.getName(),
                    profile.getSurname(), profile.getBio(), photoFileName));
        }
    }

    public Optional<List<MiniProfileResponse>> findProfilesByUsername(String username){
        Optional<List<User>> users = userService.findUsersBySimilarUsername(username);
        List<MiniProfileResponse> profilesResponse = new ArrayList<>();
        String photoFileName;
        if (users.get().isEmpty()){
            return Optional.empty();
        }
        else {
            for (User user : users.get()){
                try {
                    photoFileName = user.getProfile().getPhoto().getFileName();
                } catch (Exception e) {
                    photoFileName = null;
                }
                profilesResponse.add(new MiniProfileResponse(
                        user.getId(), user.getUsername(), user.getProfile().getName(),
                        user.getProfile().getSurname(), photoFileName
                ));
            }
            return Optional.of(profilesResponse);
        }
    }

}
