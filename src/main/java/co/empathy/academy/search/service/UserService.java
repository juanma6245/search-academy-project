package co.empathy.academy.search.service;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;

import java.util.List;

public interface UserService {
    void save(User user) throws ExistingUserException;

    void delete(Long id) throws UserNotFoundException;

    void update(User user) throws UserNotFoundException;
    User get(Long id) throws UserNotFoundException;
    List<User> getAll();
}
