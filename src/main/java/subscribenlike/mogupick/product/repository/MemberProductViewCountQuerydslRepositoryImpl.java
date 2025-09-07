package subscribenlike.mogupick.product.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import subscribenlike.mogupick.brand.domain.QBrand;
import subscribenlike.mogupick.member.domain.QMember;
import subscribenlike.mogupick.product.domain.QMemberProductViewCount;
import subscribenlike.mogupick.product.domain.QProduct;
import subscribenlike.mogupick.product.domain.QProductMedia;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;
import subscribenlike.mogupick.review.domain.QReview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberProductViewCountQuerydslRepositoryImpl implements MemberProductViewCountQuerydslRepository {

    private final JPAQueryFactory query;

    public List<RecentlyViewProductsQueryResult> findRecentlyViewedProductsByMemberId(Pageable pageable, Long memberId) {
        return query.select(Projections.constructor(RecentlyViewProductsQueryResult.class,
                        QProduct.product.id,
                        QProductMedia.productMedia.imageUrl,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.brand.id,
                        QProduct.product.brand.name,
                        QProduct.product.createdAt,
                        QReview.review.score.avg().coalesce(0.0),
                        QReview.review.count().coalesce(0L),
                        QMemberProductViewCount.memberProductViewCount.viewCount,
                        QMemberProductViewCount.memberProductViewCount.lastViewedAt
                ))
                .from(QMemberProductViewCount.memberProductViewCount)
                .join(QMemberProductViewCount.memberProductViewCount.product, QProduct.product)
                .join(QMemberProductViewCount.memberProductViewCount.member, QMember.member)
                .leftJoin(QProduct.product.brand, QBrand.brand)
                .leftJoin(QProductMedia.productMedia).on(QProductMedia.productMedia.product.eq(QProduct.product))
                .leftJoin(QReview.review).on(QReview.review.product.eq(QProduct.product))
                .where(QMember.member.id.eq(memberId))
                .groupBy(
                        QProduct.product.id,
                        QProductMedia.productMedia.imageUrl,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.brand.id,
                        QProduct.product.brand.name,
                        QMemberProductViewCount.memberProductViewCount.viewCount,
                        QMemberProductViewCount.memberProductViewCount.lastViewedAt
                )
                .orderBy(QMemberProductViewCount.memberProductViewCount.lastViewedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
