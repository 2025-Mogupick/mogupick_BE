package subscribenlike.mogupick.deliveryAddress.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressName;

    private String baseAddress;

    private String detailAddress;

    private String receiver;

    private String contact;

    @ManyToOne
    private Member member;

    public DeliveryAddress(Member member, String addressName, String baseAddress, String detailAddress, String receiver, String contact) {
        this.member = member;
        this.addressName = addressName;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.contact = contact;
    }

    public static DeliveryAddress of(Member member, String addressName, String  baseAddress, String detailAddress,
                                     String receiver, String contact) {
        return new DeliveryAddress(member, addressName, baseAddress, detailAddress, receiver, contact);
    }

    public void update(String addressName, String baseAddress, String detailAddress,
                       String receiver, String contact) {
        this.addressName = addressName;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.contact = contact;
    }
}
