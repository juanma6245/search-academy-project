package co.empathy.academy.search.service;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class UserServiceImpl implements UserService{

    private ConcurrentHashMap<Long, User> map;

    public UserServiceImpl() {
        this.map = new ConcurrentHashMap<>();
    }
    @Override
    public void save(User user) throws ExistingUserException {
        if (!this.map.containsKey(user.getId())) {
            this.map.put(user.getId(), user);
        } else {
            throw new ExistingUserException("User already in memory");
        }
    }

    @Override
    public void delete(Long id) throws UserNotFoundException {
        if (this.map.containsKey(id)) {
            this.map.remove(id);
        } else {
            throw new UserNotFoundException("Can't find user");
        }
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        if (this.map.containsKey(user.getId())) {
            this.map.replace(user.getId(), user);
        } else {
            throw new UserNotFoundException("Can't find user");
        }
    }

    @Override
    public User get(Long id) throws UserNotFoundException {
        User response = null;
        if (this.map.containsKey(id)) {
            response = this.map.get(id);
        } else {
            throw new UserNotFoundException("Can't find user");
        }

        return response;
    }

    @Override
    public List<User> getAll() {
        List<User> response = new ArrayList<>();
        for (Map.Entry<Long, User> entry : this.map.entrySet()) {
            response.add(entry.getValue());
        }
        return response;
    }
}
