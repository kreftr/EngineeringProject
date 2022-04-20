package edu.pjatk.app.registration;

import edu.pjatk.app.email.activation_token.ActivationToken;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.request.RegistrationRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/registration")
@CrossOrigin("http://localhost:3000")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;
    private final ActivationTokenService activationTokenService;

    @Autowired
    public RegistrationController(RegistrationService registrationService,
                                  UserService userService,
                                  ActivationTokenService activationTokenService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.activationTokenService = activationTokenService;
    }


    @PostMapping
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequest request){
        if (userService.findUserByUsername(request.getUsername()).isPresent()){
            return new ResponseEntity(
                    new ResponseMessage("User with username "+request.getUsername()+" already exists"),
                            HttpStatus.CONFLICT
            );
        }
        else if (userService.findUserByEmail(request.getEmail()).isPresent()){
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
                    new ResponseMessage("Invalid token"), HttpStatus.BAD_REQUEST
            );
        }
        else if (activationToken.get().getConfirmed() != null){
            return new ResponseEntity(
                    new ResponseMessage("Token has been already verified"), HttpStatus.CONFLICT
            );
        }
        else if (LocalDateTime.now().isAfter(activationToken.get().getExpired())){
            return new ResponseEntity(
                    new ResponseMessage("Token expired"), HttpStatus.GONE
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
