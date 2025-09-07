package subscribenlike.mogupick.subscription.dto;

import java.time.LocalDate;
import java.util.List;

public record CalendarDayResponse(
        LocalDate date,
        List<CalendarSubscriptionResponse> subscriptions
) {}
