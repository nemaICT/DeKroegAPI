package dekroeg.exception;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {

    protected String title;
    protected int status;
    protected String detail_info;
    protected LocalDateTime timestamp;
    protected String developerMessage;
}
