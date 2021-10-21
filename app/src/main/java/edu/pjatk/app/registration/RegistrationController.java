package edu.pjatk.app.registration;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }


    @PostMapping
    public ResponseEntity registerUser(@RequestBody RegistrationRequest request){
        if (registrationService.thatUsernameAlreadyExists(request.getUsername())){
            return new ResponseEntity(
                    new ResponseMessage("User with username "+request.getUsername()+" already exists"),
                            HttpStatus.CONFLICT
            );
        }
        else if (registrationService.thatEmailAlreadyExists(request.getEmail())){
            return new ResponseEntity(
                    new ResponseMessage("User with email "+request.getEmail()+" already exists"),
                    HttpStatus.CONFLICT
            );
        }
        else {
            try {
                registrationService.registerUser(request);
                return new ResponseEntity(
                        new ResponseMessage("User successfully created"), HttpStatus.CREATED);
            } catch (Exception e){
                return new ResponseEntity(
                        new ResponseMessage("Oops something went wrong"),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }


}
