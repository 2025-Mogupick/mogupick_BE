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
import java.util.stream.Collectors;

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
        DeliveryAddress saved = addressRepository.save(address);
        return DeliveryAddressResponse.from(saved);
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

        if (!address.getMember().getId().equals(memberId)) {
            throw new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED);
        }

        address = new DeliveryAddress(
                address.getMember(),
                req.baseAddress(),
                req.detailAddress(),
                req.receiver(),
                req.contact()
        );
        DeliveryAddress updated = addressRepository.save(address);
        return DeliveryAddressResponse.from(updated);
    }

    @Transactional
    public void delete(Long memberId, Long addressId) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_NOT_FOUND));

        if (!address.getMember().getId().equals(memberId)) {
            throw new DeliveryAddressException(DeliveryAddressErrorCode.ADDRESS_UNAUTHORIZED);
        }

        addressRepository.delete(address);
    }
}
