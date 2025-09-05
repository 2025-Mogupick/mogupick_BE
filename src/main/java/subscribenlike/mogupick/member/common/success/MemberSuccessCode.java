package subscribenlike.mogupick.member.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberSuccessCode implements SuccessCode {

    // 200 OK
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "내 정보 조회에 성공했습니다."),
    UPDATE_NICKNAME_SUCCESS(HttpStatus.OK, "닉네임 수정에 성공했습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}