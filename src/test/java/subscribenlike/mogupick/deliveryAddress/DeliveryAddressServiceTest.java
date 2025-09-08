package subscribenlike.mogupick.deliveryAddress;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.billing.util.AesGcm;
import subscribenlike.mogupick.common.utils.S3Service;
import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressRequest;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressResponse;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressUpdateRequest;
import subscribenlike.mogupick.deliveryAddress.repository.DeliveryAddressRepository;
import subscribenlike.mogupick.deliveryAddress.service.DeliveryAddressService;
import subscribenlike.mogupick.global.oauth.client.GoogleApiClient;
import subscribenlike.mogupick.global.oauth.client.KakaoApiClient;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.DeliveryAddressFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;
import subscribenlike.mogupick.deliveryAddress.common.exception.DeliveryAddressException;
import subscribenlike.mogupick.deliveryAddress.common.exception.DeliveryAddressErrorCode;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ServiceTest
public class DeliveryAddressServiceTest {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private S3Service s3Service;

    @MockitoBean
    private DeliveryAddressRepository addressRepository;

    @MockitoBean
    private MemberRepository memberRepository;

    @MockitoBean
    private GoogleApiClient googleApiClient;

    @MockitoBean
    private KakaoApiClient kakaoApiClient;

    @MockitoBean
    private WebClient tossWebClient;

    @MockitoBean
    private AesGcm aesGcm;

    @Test
    void 배송지_등록_성공() {
        Member member = MemberFixture.김회원();
        DeliveryAddress address = DeliveryAddressFixture.기본배송지.배송지(member);
        DeliveryAddressRequest request = new DeliveryAddressRequest(
                address.getBaseAddress(),
                address.getDetailAddress(),
                address.getReceiver(),
                address.getContact()
        );

        when(memberRepository.findOrThrow(member.getId())).thenReturn(member);
        when(addressRepository.save(any(DeliveryAddress.class))).thenReturn(address);

        DeliveryAddressResponse response = deliveryAddressService.register(member.getId(), request);

        assertThat(response.receiver()).isEqualTo(address.getReceiver());
        assertThat(response.baseAddress()).isEqualTo(address.getBaseAddress());
        assertThat(response.detailAddress()).isEqualTo(address.getDetailAddress());
        assertThat(response.contact()).isEqualTo(address.getContact());
    }

    @Test
    void 배송지_전체조회_성공() {
        Member member = MemberFixture.김회원();
        DeliveryAddress address1 = DeliveryAddressFixture.기본배송지.배송지(member);
        DeliveryAddress address2 = DeliveryAddressFixture.두번째배송지.배송지(member);

        when(addressRepository.findByMemberId(member.getId())).thenReturn(List.of(address1, address2));

        List<DeliveryAddressResponse> responses = deliveryAddressService.findAllByMemberId(member.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("receiver").contains(address1.getReceiver(), address2.getReceiver());
    }

    @Test
    void 배송지_수정_성공() {
        Member member = MemberFixture.김회원();
        DeliveryAddress oldAddress = DeliveryAddressFixture.기본배송지.배송지(member);
        DeliveryAddressUpdateRequest updateReq = new DeliveryAddressUpdateRequest(
                "수정된 기본주소",
                "수정된 상세주소",
                "수정된 수령인",
                "010-1111-2222"
        );
        DeliveryAddress updatedAddress = new DeliveryAddress(
                member,
                updateReq.baseAddress(),
                updateReq.detailAddress(),
                updateReq.receiver(),
                updateReq.contact()
        );

        when(addressRepository.findById(oldAddress.getId())).thenReturn(Optional.of(oldAddress));
        when(addressRepository.save(any(DeliveryAddress.class))).thenReturn(updatedAddress);

        DeliveryAddressResponse response = deliveryAddressService.update(member.getId(), oldAddress.getId(), updateReq);

        assertThat(response.baseAddress()).isEqualTo(updateReq.baseAddress());
        assertThat(response.detailAddress()).isEqualTo(updateReq.detailAddress());
        assertThat(response.receiver()).isEqualTo(updateReq.receiver());
        assertThat(response.contact()).isEqualTo(updateReq.contact());
    }

    @Test
    void 배송지_수정_존재하지_않는_주소() {
        when(addressRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryAddressService.update(1L, 1L,
                new DeliveryAddressUpdateRequest("a", "b", "c", "d")))
                .isInstanceOf(DeliveryAddressException.class)
                .hasMessageContaining(DeliveryAddressErrorCode.ADDRESS_NOT_FOUND.getMessage());
    }

    @Test
    void 배송지_수정_권한없음() {
        Member owner = MemberFixture.김모구();
        DeliveryAddress address = DeliveryAddressFixture.기본배송지.배송지(owner);

        Long addressId = 200L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        Long callerId = 1L;

        assertThatThrownBy(() -> deliveryAddressService.update(
                callerId, addressId, new DeliveryAddressUpdateRequest("a","b","c","d")
        ))
                .isInstanceOf(DeliveryAddressException.class)
                .hasMessageContaining(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED.getMessage());
    }

    @Test
    void 배송지_삭제_성공() {
        Member member = MemberFixture.김회원();
        DeliveryAddress address = DeliveryAddressFixture.기본배송지.배송지(member);
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        deliveryAddressService.delete(member.getId(), address.getId());

        // 삭제는 void라 별도 결과 검증 대신 Mockito verify를 추가하려면 가능
    }

    @Test
    void 배송지_삭제_존재하지_않는_주소() {
        when(addressRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryAddressService.delete(1L, 1L))
                .isInstanceOf(DeliveryAddressException.class)
                .hasMessageContaining(DeliveryAddressErrorCode.ADDRESS_NOT_FOUND.getMessage());
    }

    @Test
    void 배송지_삭제_권한없음() {
        Member owner = MemberFixture.김모구();
        DeliveryAddress address = DeliveryAddressFixture.기본배송지.배송지(owner);

        Long addressId = 400L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        Long callerId = 1L;

        assertThatThrownBy(() -> deliveryAddressService.delete(callerId, addressId))
                .isInstanceOf(DeliveryAddressException.class)
                .hasMessageContaining(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED.getMessage());
    }
}
