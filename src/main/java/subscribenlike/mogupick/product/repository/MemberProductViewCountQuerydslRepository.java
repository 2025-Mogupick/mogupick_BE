package subscribenlike.mogupick.product.repository;


import org.springframework.data.domain.Pageable;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;

import java.util.List;

public interface MemberProductViewCountQuerydslRepository {
    List<RecentlyViewProductsQueryResult> findRecentlyViewedProductsByMemberId(Pageable pageable, Long memberId);
}
