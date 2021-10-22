package edu.pjatk.app.registration;

import edu.pjatk.app.email.token.ActivationToken;
import edu.pjatk.app.email.token.ActivationTokenService;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final ActivationTokenService activationTokenService;

    @Autowired
    public RegistrationController(RegistrationService registrationService, ActivationTokenService activationTokenService) {
        this.registrationService = registrationService;
        this.activationTokenService = activationTokenService;
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
                registrationService.registerUser(request);
                return new ResponseEntity(
                        new ResponseMessage("User successfully created"), HttpStatus.CREATED);
        }
    }

    @GetMapping
    @RequestMapping("/verify")
    public ResponseEntity verifyToken(@RequestParam String token){

        Optional<ActivationToken> activationToken = activationTokenService.findActivationTokenByToken(token);

        if (activationToken.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Bad token"), HttpStatus.BAD_REQUEST
            );
        }
        else if (LocalDateTime.now().isAfter(activationToken.get().getExpired())){
            return new ResponseEntity(
                    new ResponseMessage("Token expired"), HttpStatus.GONE
            );
        }
        else if (activationToken.get().getConfirmed() != null){
            return new ResponseEntity(
                    new ResponseMessage("Token has been already verified"), HttpStatus.CONFLICT
            );
        }
        else {
            registrationService.verifyAccount(activationToken.get());
            return new ResponseEntity(
                    new ResponseMessage("Token confirmed, account activated"), HttpStatus.OK
            );
        }
    }

}
