package br.com.douglas.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = userRepository.findByUsername(userModel.getUsername());
        if (user !=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário ja existe");
        }
        var passwdHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwdHashred);
        var created = userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}