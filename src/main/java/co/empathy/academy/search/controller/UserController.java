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

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user){
        ResponseEntity<User> response = null;
        try {
            this.userService.save(user);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ExistingUserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already in memory", e);
        }
        return response;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        ResponseEntity<List<User>> response = new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
        return response;
    }
}
