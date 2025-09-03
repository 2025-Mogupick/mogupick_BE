package subscribenlike.mogupick.global.dto;

import lombok.Getter;

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

    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(200, "success", data);
    }

    public static GlobalResponse<?> error(int status, String message) {
        return new GlobalResponse<>(status, message, null);
    }
}