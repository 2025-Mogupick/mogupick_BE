package subscribenlike.mogupick.order.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressSnapshot {
    private String receiver;
    private String contact;
    private String baseAddress;
    private String detailAddress;

    public static AddressSnapshot of(String n, String p, String z, String a1) {
        return new AddressSnapshot(n, p, z, a1);
    }
}
