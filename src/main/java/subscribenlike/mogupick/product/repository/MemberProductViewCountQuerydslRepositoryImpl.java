package subscribenlike.mogupick.product.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import subscribenlike.mogupick.brand.domain.QBrand;
import subscribenlike.mogupick.member.domain.QMember;
import subscribenlike.mogupick.product.domain.QMemberProductViewCount;
import subscribenlike.mogupick.product.domain.QProduct;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;
import subscribenlike.mogupick.review.domain.QReview;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberProductViewCountQuerydslRepositoryImpl implements MemberProductViewCountQuerydslRepository {

    private final JPAQueryFactory query;

    public Page<RecentlyViewProductsQueryResult> findRecentlyViewedProductsByMemberId(Pageable pageable, Long memberId) {
        List<RecentlyViewProductsQueryResult> content = query
                .select(Projections.constructor(RecentlyViewProductsQueryResult.class,
                        QProduct.product.id,
                        QProduct.product.imageUrl,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.brand.id,
                        QProduct.product.brand.name,
                        QReview.review.score.avg().coalesce(0.0),
                        QReview.review.count().coalesce(0L),
                        QMemberProductViewCount.memberProductViewCount.viewCount,
                        QMemberProductViewCount.memberProductViewCount.lastViewedAt
                ))
                .from(QMemberProductViewCount.memberProductViewCount)
                .join(QMemberProductViewCount.memberProductViewCount.product, QProduct.product)
                .join(QMemberProductViewCount.memberProductViewCount.member, QMember.member)
                .leftJoin(QProduct.product.brand, QBrand.brand)
                .leftJoin(QReview.review).on(QReview.review.product.eq(QProduct.product))
                .where(QMember.member.id.eq(memberId))
                .groupBy(
                        QProduct.product.id,
                        QProduct.product.imageUrl,
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
                .fetch()
                .stream()
                .map(result -> new RecentlyViewProductsQueryResult(
                        result.getProductId(),
                        null, // TODO: 다중 이미지 지원 시 구현
                        result.getProductName(),
                        result.getProductPrice(),
                        result.getBrandId(),
                        result.getBrandName(),
                        result.getRating(),
                        result.getReviewCount(),
                        result.getViewCount(),
                        result.getLastViewedAt()
                ))
                .collect(Collectors.toList());

        Long total = query
                .select(QMemberProductViewCount.memberProductViewCount.count())
                .from(QMemberProductViewCount.memberProductViewCount)
                .join(QMemberProductViewCount.memberProductViewCount.member, QMember.member)
                .where(QMember.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
