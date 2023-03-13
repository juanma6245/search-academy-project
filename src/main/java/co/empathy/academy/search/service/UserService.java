package co.empathy.academy.search.service;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    void save(User user) throws ExistingUserException;

    void delete(Long id) throws UserNotFoundException;

    void update(User user) throws UserNotFoundException;
    User get(Long id) throws UserNotFoundException;
    List<User> getAll();
    CompletableFuture saveFile(MultipartFile file) throws IOException, ExistingUserException, InterruptedException;
}
