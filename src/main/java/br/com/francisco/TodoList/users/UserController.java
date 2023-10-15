package br.com.francisco.TodoList.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
       var user = this.userRepository.findByUsername(userModel.getUsername());
       
       if(user != null){
        System.out.println("Usuário já existe");
        return ResponseEntity.status(400).body("Usuário já existe");
       }

      var password = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());       
      
      userModel.setPassword(password);
      var userCreated = this.userRepository.save(userModel);
            return ResponseEntity.status(200).body(userCreated);

    }
}
