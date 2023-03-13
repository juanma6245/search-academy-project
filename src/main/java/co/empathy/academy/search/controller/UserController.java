package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import co.empathy.academy.search.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get users by Id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404",
                    description = "Can't find user", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        User user;
        ResponseEntity<User> response = null;

        user = userService.get(id);
        response = new ResponseEntity<>(user, HttpStatus.OK);

        return response;
    }

    @Operation(summary = "Modify user by Id", responses = {
            @ApiResponse(responseCode = "204",
                    description = "User modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404",
                    description = "Can't find user", content = @Content(mediaType = "text/plain"))
    })
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

    @Operation(summary = "Save a new user from request body", responses = {
            @ApiResponse(responseCode = "201",
                    description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400",
                    description = "User already in memory", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) throws ExistingUserException {
        ResponseEntity<User> response = null;
        //Validations should be here

        this.userService.save(user);
        response = new ResponseEntity<>(user, HttpStatus.CREATED);

        return response;
    }

    @Operation(summary = "Create users from a json file", responses = {
            @ApiResponse(responseCode = "202",
                    description = "",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400",
                    description = "User already in memory", content = @Content(mediaType = "text"))
    })
    @PostMapping("/file")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ExistingUserException, InterruptedException {
        CompletableFuture users = this.userService.saveFile(file);
    }

    @Operation(summary = "Delete user by Id", responses = {
            @ApiResponse(responseCode = "204",
                    description = "User deleted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404",
                    description = "Can't find user", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        ResponseEntity<User> response = null;
        this.userService.delete(id);
        response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return response;
    }

    @Operation(summary = "Get all users", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        ResponseEntity<List<User>> response = new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
        return response;
    }
}
