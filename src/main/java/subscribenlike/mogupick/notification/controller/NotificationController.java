package subscribenlike.mogupick.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.auth.domain.PrincipalDetails;
import subscribenlike.mogupick.global.dto.GlobalResponse;
import subscribenlike.mogupick.notification.common.success.NotificationSuccessCode;
import subscribenlike.mogupick.notification.dto.NotificationResponse;
import subscribenlike.mogupick.notification.service.NotificationService;

import java.util.List;

@Tag(name = "Notification", description = "알림 관련 API")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "내 알림 목록 조회", description = "현재 로그인된 사용자의 알림 목록을 최신순으로 조회합니다.")
    @GetMapping
    public GlobalResponse<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<NotificationResponse> notifications = notificationService.getNotifications(principalDetails.getId());
        return GlobalResponse.from(NotificationSuccessCode.GET_NOTIFICATIONS_SUCCESS, notifications);
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/{notificationId}")
    public GlobalResponse<Void> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        notificationService.readNotification(notificationId, principalDetails.getId());
        return GlobalResponse.from(NotificationSuccessCode.READ_NOTIFICATION_SUCCESS);
    }

    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    @DeleteMapping("/{notificationId}")
    public GlobalResponse<Void> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        notificationService.deleteNotification(notificationId, principalDetails.getId());
        return GlobalResponse.from(NotificationSuccessCode.DELETE_NOTIFICATION_SUCCESS);
    }
}