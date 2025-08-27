package subscribenlike.mogupick.common.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    HttpStatus getStatus();
    String getMessage();
}
