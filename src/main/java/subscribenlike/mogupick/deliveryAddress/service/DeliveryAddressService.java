package subscribenlike.mogupick.deliveryAddress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.deliveryAddress.common.exception.DeliveryAddressErrorCode;
import subscribenlike.mogupick.deliveryAddress.common.exception.DeliveryAddressException;
import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressRequest;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressResponse;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressUpdateRequest;
import subscribenlike.mogupick.deliveryAddress.repository.DeliveryAddressRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DeliveryAddressResponse register(Long memberId, DeliveryAddressRequest req) {
        Member member = memberRepository.findOrThrow(memberId);

        DeliveryAddress address = DeliveryAddress.of(
                member,
                req.baseAddress(),
                req.detailAddress(),
                req.receiver(),
                req.contact()
        );

        // JPA에선 dirty checking으로도 가능하지만, 테스트 목과의 호환을 위해 save 호출 유지
        addressRepository.save(address);
        return DeliveryAddressResponse.from(address);
    }

    @Transactional(readOnly = true)
    public List<DeliveryAddressResponse> findAllByMemberId(Long memberId) {
        return addressRepository.findByMemberId(memberId).stream()
                .map(DeliveryAddressResponse::from)
                .toList();
    }

    @Transactional
    public DeliveryAddressResponse update(Long memberId, Long addressId, DeliveryAddressUpdateRequest req) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_NOT_FOUND));

        // ✅ 권한 체크 최우선 + null-safe
        if (!Objects.equals(ownerIdOf(address), memberId)) {
            throw new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED);
        }

        // ✅ 기존 엔티티를 수정하여 id 보존
        address.update(
                req.baseAddress(),
                req.detailAddress(),
                req.receiver(),
                req.contact()
        );

        // 목 환경에서도 문제 없도록 save 호출 (운영에선 dirty checking으로 flush)
        addressRepository.save(address);

        return DeliveryAddressResponse.from(address);
    }

    @Transactional
    public void delete(Long memberId, Long addressId) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_NOT_FOUND));

        if (!Objects.equals(ownerIdOf(address), memberId)) {
            throw new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED);
        }

        addressRepository.delete(address);
    }

    private static Long ownerIdOf(DeliveryAddress address) {
        return (address != null && address.getMember() != null) ? address.getMember().getId() : null;
    }
}
