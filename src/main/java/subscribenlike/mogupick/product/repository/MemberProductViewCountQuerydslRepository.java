package subscribenlike.mogupick.product.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;

public interface MemberProductViewCountQuerydslRepository {
    Page<RecentlyViewProductsQueryResult> findRecentlyViewedProductsByMemberId(Pageable pageable, Long memberId);
}
