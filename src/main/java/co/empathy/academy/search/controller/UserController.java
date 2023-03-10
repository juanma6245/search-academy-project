package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import co.empathy.academy.search.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        User user;
        ResponseEntity<User> response = null;

        user = userService.get(id);
        response = new ResponseEntity<>(user, HttpStatus.OK);

        return response;
    }

    @PutMapping("{id}")
    public ResponseEntity<User> modifyUser(@PathVariable("id") Long id, @RequestBody User newData) throws UserNotFoundException {
        ResponseEntity<User> response = null;
        User user;

        user = this.userService.get(id);
        if (newData.getName() != null) {
            user.setName(newData.getName());
        }
        if (newData.getEmail() != null) {
            user.setEmail(newData.getEmail());
        }
        this.userService.update(user);
        response = new ResponseEntity<>(user, HttpStatus.NO_CONTENT);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) throws ExistingUserException {
        ResponseEntity<User> response = null;
        //Validations should be here

        this.userService.save(user);
        response = new ResponseEntity<>(user, HttpStatus.CREATED);

        return response;
    }

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Async
    public Future<ResponseEntity<?>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ExistingUserException, InterruptedException, ExecutionException {
        Future<ResponseEntity<?>> response = new AsyncResult<>(ResponseEntity.status(HttpStatus.OK).build());
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(file.getInputStream(), new TypeReference<List<User>>(){});
        //Thread.sleep(10000);
        for (User user: users) {
            this.userService.save(user);
            //Thread.sleep(1000);
        }
        return response;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        ResponseEntity<User> response = null;
        this.userService.delete(id);
        response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return response;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        ResponseEntity<List<User>> response = new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
        return response;
    }
}
