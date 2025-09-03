package subscribenlike.mogupick.product.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import subscribenlike.mogupick.member.domain.QMember;
import subscribenlike.mogupick.product.domain.QProduct;
import subscribenlike.mogupick.product.domain.QProductViewCount;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberProductViewCountQuerydslRepositoryImpl implements MemberProductViewCountQuerydslRepository {

    private final JPAQueryFactory query;

    public Page<ProductsInMonthQueryResult> findAllProductsByMemberId(Pageable pageable, Long memberId) {
        return query
                .select(Projections.constructor(RecentlyViewProductsQueryResult.class,
                        // TODO: RecentlyViewProductsQueryResult.class의 필드
                        ))
                .from(QMemberProductViewCount.memberProductViewCount)
                .join(QProduct.product).on(QMemberProductViewCount.memberProductViewCount.product.eq(QProduct.product))
                .join(QMember.member).on(QMemberProductViewCount.memberProductViewCount.member.eq(QMember.member))
                .where(QMember.member.id.eq(memberId))
                .fetch();
    }
}
