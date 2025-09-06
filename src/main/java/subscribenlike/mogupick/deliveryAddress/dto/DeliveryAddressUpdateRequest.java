package subscribenlike.mogupick.deliveryAddress.dto;

public record DeliveryAddressUpdateRequest(
        String baseAddress,
        String detailAddress,
        String receiver,
        String contact
) {}
