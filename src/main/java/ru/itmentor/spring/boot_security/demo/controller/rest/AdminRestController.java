package ru.itmentor.spring.boot_security.demo.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> helloAdmin(Principal principal){
        List<User> userList = userService.findAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        User user = userService.getUserById(id);
        if (user != null){
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clear-table")
    public ResponseEntity<Void> cleanUsersTable(){
        userService.clearUsers();
        return ResponseEntity.noContent().build();
    }
}
