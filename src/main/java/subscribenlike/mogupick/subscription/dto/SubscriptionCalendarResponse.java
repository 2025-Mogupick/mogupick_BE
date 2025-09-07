package subscribenlike.mogupick.subscription.dto;

import java.util.List;

public record SubscriptionCalendarResponse(
        int totalAmount,
        List<CalendarDayResponse> days
) {}
