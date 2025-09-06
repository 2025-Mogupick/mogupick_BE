package subscribenlike.mogupick.billing.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CryptoErrorCode implements ErrorCode {

    AES_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AES-GCM 암호화 실패"),
    AES_DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AES-GCM 복호화 실패");

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[CRYPTO ERROR] " + rawMessage;
    }
}
