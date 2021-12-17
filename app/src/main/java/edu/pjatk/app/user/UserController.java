package edu.pjatk.app.user;

import edu.pjatk.app.request.PasswordChangeRequest;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/deleteAccount")
    public ResponseEntity deleteAccount(){
        userService.removeCurrentlyLoggedUser();
        return new ResponseEntity(new ResponseMessage("Account has been deleted"), HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@Valid @RequestBody PasswordChangeRequest passwordRequest) {
        userService.changeUserPassword(passwordRequest);
        return new ResponseEntity(new ResponseMessage("Password has been changed"), HttpStatus.OK);
    }

    @GetMapping(value = "/findUserById/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent())
        {
            return new ResponseEntity<>(
                    user, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There is no user with that id!"), HttpStatus.NOT_FOUND
            );
        }
    }

}
