package dekroeg.handler;

import dekroeg.exception.ExceptionDetails;
import dekroeg.exception.ResourceNotFoundDetails;
import dekroeg.exception.ResourceNotFoundException;
import dekroeg.exception.ValidationExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*The @ControllerAdvice annotation was first introduced in Spring 3.2.
 It allows you to handle exceptions across the whole application, not just to an individual controller.
 You can think of it as an interceptor of exceptions thrown by methods annotated with
 @RequestMapping or one of the shortcuts*/
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDetails> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){

        return new ResponseEntity<>(
                ResourceNotFoundDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail_info(resourceNotFoundException.getMessage())
                .developerMessage(resourceNotFoundException.getClass().getName())
                .build(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                                                                MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
        //log.info("Fields error {}", methodArgumentNotValidException.getBindingResult().getFieldErrors());


        List<FieldError> fieldsErros = ex.getBindingResult().getFieldErrors();

        String fields = fieldsErros.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldsErros.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Field Validation Error")
                        .detail_info(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

       ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .title(ex.getCause().getMessage())
                .detail_info(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        return new ResponseEntity(exceptionDetails, headers, status);
    }
}
