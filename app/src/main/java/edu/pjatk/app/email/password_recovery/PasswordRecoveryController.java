package edu.pjatk.app.email.password_recovery;

import edu.pjatk.app.request.PasswordChangeRequest;
import edu.pjatk.app.request.PasswordRecoveryRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/recovery")
@CrossOrigin("http://localhost:3000")
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;
    private final UserService userService;

    @Autowired
    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService, UserService userService){
        this.passwordRecoveryService = passwordRecoveryService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity sendPasswordRecoveryToken(@Valid @RequestBody PasswordRecoveryRequest recoveryRequest){

        Optional<User> user = userService.findUserByEmail(recoveryRequest.getEmail());

        if (user.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Email not found"), HttpStatus.NOT_FOUND);
        }
        else if (passwordRecoveryService.tokenAlreadySent(user.get())){
            return new ResponseEntity(
                new ResponseMessage("Recovery link has been already sent"), HttpStatus.CONFLICT);
        }
        else{
            passwordRecoveryService.sendToken(user.get());
            return new ResponseEntity(
                    new ResponseMessage(
                            "Check your e-mail, a message with a link to reset your password has been sent"),
                    HttpStatus.OK);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestParam("token") String token,
                                        @Valid @RequestBody PasswordChangeRequest passwordChangeRequest){

        if (!passwordRecoveryService.tokenIsValid(token)){
            return new ResponseEntity(new ResponseMessage("Invalid token"), HttpStatus.UNAUTHORIZED);
        }
        else {
            passwordRecoveryService.resetPassword(token, passwordChangeRequest);
            return new ResponseEntity(new ResponseMessage("Password has been changed"), HttpStatus.OK);
        }
    }

}
