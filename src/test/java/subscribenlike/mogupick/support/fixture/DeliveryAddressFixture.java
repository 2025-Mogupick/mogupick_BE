package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;
import subscribenlike.mogupick.member.domain.Member;

public enum DeliveryAddressFixture {
    기본배송지("서울특별시 강남구", "101동 1001호", "홍길동", "010-1234-5678"),
    두번째배송지("서울특별시 서초구", "101동 101호", "김회원", "010-1111-1111")
    ;

    private final String baseAddress;
    private final String detailAddress;
    private final String receiver;
    private final String contact;

    DeliveryAddressFixture(String baseAddress, String detailAddress, String receiver, String contact) {
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.contact = contact;
    }

    public DeliveryAddress 배송지(Member member) {
        return new DeliveryAddress(member, baseAddress, detailAddress, receiver, contact);
    }
}
