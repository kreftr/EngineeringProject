package edu.pjatk.app.user.profile;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.project.category.CategoryService;
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
import java.util.*;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PhotoService photoService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, PhotoService photoService, UserService userService,
                          CategoryService categoryService){
        this.profileRepository = profileRepository;
        this.photoService = photoService;
        this.userService = userService;
        this.categoryService = categoryService;
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
        Set<Category> categories = new HashSet<>();
        for (String category : editRequest.getCategories()){
            categories.add(categoryService.getCategoryByTitle(category).get());
        }
        userProfile.setCategories(categories);

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
            Profile profile = user.get().getProfile();
            try {
                photoFileName = profile.getPhoto().getFileName();
            } catch (Exception e) {
                photoFileName = null;
            }

            Set<String> categories = new HashSet<>();
            for (Category c : profile.getCategories()) categories.add(c.getTitle());

            return Optional.of(new FullProfileResponse(user.get().getUsername(), profile.getName(),
                    profile.getSurname(), profile.getBio(), photoFileName, categories));
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

    public Long getProfilesNumberByUsername(String username) {
        return userService.getUsersNumber(username);
    }

    public Optional<List<MiniProfileResponse>> findProfilesWithPagination(String username, int pageNumber, int pageSize) {
        Optional<List<User>> users = userService.findUsersWithPagination(username, pageNumber, pageSize);
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
