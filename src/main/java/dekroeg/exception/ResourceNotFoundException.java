package dekroeg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Create a Custom ResourceNotFoundException where we can throw a custom message to the user
// extending the RuntimeException and having a constructor returning a custom message
// also always return a 404 status using the annotation @ResponseStatus(HttpStatus.NOT_FOUND)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
