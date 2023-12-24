package by.youngliqui.EShopProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizeException extends RuntimeException {
    public UserNotAuthorizeException(String message) {
        super(message);
    }
}
