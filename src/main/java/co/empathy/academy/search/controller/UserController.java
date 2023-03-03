package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import co.empathy.academy.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathParam("id") Long id) {
        User userResponse;
        ResponseEntity<User> response = null;
        try {
             userResponse = userService.get(id);
             response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }
}
