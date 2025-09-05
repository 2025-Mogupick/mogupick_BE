package subscribenlike.mogupick.global.dto;

import lombok.Getter;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
public class GlobalResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    private GlobalResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static GlobalResponse<?> error(int status, String message) {
        return new GlobalResponse<>(status, message, null);
    }

    public static <T> GlobalResponse<T> from(SuccessCode successCode, T data) {
        return new GlobalResponse<>(
                successCode.getStatus().value(),
                successCode.getMessage(),
                data
        );
    }

    public static GlobalResponse<Void> from(SuccessCode successCode) {
        return new GlobalResponse<>(
                successCode.getStatus().value(),
                successCode.getMessage(),
                null
        );
    }
}