package subscribenlike.mogupick.subscriptionOption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.subscriptionOption.dto.SubscriptionOptionResponse;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionOptionService {
    private final SubscriptionOptionRepository subscriptionOptionRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionOptionResponse> getAll() {
        return subscriptionOptionRepository.findAllByOrderByUnitAscPeriodAsc()
                .stream().map(SubscriptionOptionResponse::from).toList();
    }
}
