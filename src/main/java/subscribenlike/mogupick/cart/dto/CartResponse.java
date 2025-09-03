package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.cart.domain.Cart;

import java.util.List;

public record CartResponse(
        Long cartId,
        Long memberId,
        List<CartItemResponse> items
) {
    public static CartResponse from(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(CartItemResponse::from).toList();
        return new CartResponse(cart.getId(), cart.getMember().getId(), items);
    }

    public static CartResponse of(Long cartId, Long memberId, List<CartItemResponse> items) {
        return new CartResponse(cartId, memberId, items);
    }
}
