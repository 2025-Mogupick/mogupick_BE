package subscribenlike.mogupick.deliveryAddress.dto;

public record DeliveryAddressUpdateRequest(
        String addressName,
        String baseAddress,
        String detailAddress,
        String receiver,
        String contact
) {}
