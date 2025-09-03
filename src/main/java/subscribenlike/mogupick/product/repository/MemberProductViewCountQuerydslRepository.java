package subscribenlike.mogupick.product.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;

public interface MemberProductViewCountQuerydslRepository {
    Page<ProductsInMonthQueryResult> findAllProductsByMemberId(Pageable pageable, Long memberId);
}
