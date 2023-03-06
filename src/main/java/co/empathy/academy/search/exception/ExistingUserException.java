package co.empathy.academy.search.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already in memory")
public class ExistingUserException extends Exception{
    public ExistingUserException(String message) {
        super(message);
    }
}
