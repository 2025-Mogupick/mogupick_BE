package subscribenlike.mogupick.order.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record CreateOrderRequest(
        List<Long> cartItemIds,
        Long addressId,
        Map<Long, LocalDate> firstDeliveryDates // cartItemId -> date (선택)
) {}
