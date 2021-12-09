package edu.pjatk.app.user;

import edu.pjatk.app.request.ChangePasswordRequest;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest){
        userService.changeUserPassword(passwordRequest);
        return new ResponseEntity(new ResponseMessage("Password has been changed"), HttpStatus.OK);
    }

}
