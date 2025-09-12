package subscribenlike.mogupick.deliveryAddress.dto;

public record DeliveryAddressRequest(
        String addressName,
        String baseAddress,
        String detailAddress,
        String receiver,
        String contact
) {}
