package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import co.empathy.academy.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user;
        ResponseEntity<User> response = null;
        try {
             user = userService.get(id);
             response = new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
        return response;
    }

    @PutMapping("{id}")
    public ResponseEntity<User> modifyUser(@PathVariable("id") Long id, @RequestBody User newData) {
        ResponseEntity<User> response = null;
        User user;
        try {
            user = this.userService.get(id);
            if (newData.getName() != null) {
                user.setName(newData.getName());
            }
            if (newData.getEmail() != null) {
                user.setEmail(newData.getEmail());
            }
            this.userService.update(user);
            response = new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Can't modify", e);
        }
        return response;
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user){
        ResponseEntity<User> response = null;
        //Validations should be here
        try {
            this.userService.save(user);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ExistingUserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already in memory", e);
        }
        return response;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        ResponseEntity<User> response = null;
        try {
            this.userService.delete(id);
            response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }


        return response;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        ResponseEntity<List<User>> response = new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
        return response;
    }
}
