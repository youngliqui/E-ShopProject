package by.youngliqui.EShopProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ProductNotCreatedException extends RuntimeException{
    public ProductNotCreatedException(String message) {
        super(message);
    }
}
