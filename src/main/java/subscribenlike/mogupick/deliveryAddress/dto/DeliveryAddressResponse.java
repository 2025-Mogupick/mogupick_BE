package subscribenlike.mogupick.deliveryAddress.dto;

import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;

public record DeliveryAddressResponse(
        Long id,
        String addressName,
        String baseAddress,
        String detailAddress,
        String receiver,
        String contact
) {
    public static DeliveryAddressResponse from(DeliveryAddress entity) {
        return new DeliveryAddressResponse(
                entity.getId(),
                entity.getAddressName(),
                entity.getBaseAddress(),
                entity.getDetailAddress(),
                entity.getReceiver(),
                entity.getContact()
        );
    }
}
