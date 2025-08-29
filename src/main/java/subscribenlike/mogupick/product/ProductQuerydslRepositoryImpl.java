package subscribenlike.mogupick.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subscribenlike.mogupick.brand.domain.QBrand;
import subscribenlike.mogupick.product.domain.QProduct;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;
import subscribenlike.mogupick.review.domain.QReview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepositoryImpl implements ProductQuerydslRepository {

    private final JPAQueryFactory query;

    public List<ProductsInMonthQueryResult> findAllProductsInMonth(int month) {
        return query
                .select(Projections.constructor(ProductsInMonthQueryResult.class,
                        QProduct.product.id,
                        QProduct.product.imageUrl,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.createdAt,
                        QProduct.product.brand.id,
                        QProduct.product.brand.name,
                        QReview.review.score.avg(),
                        QReview.review.count()
                        ))
                .from(QProduct.product)
                .leftJoin(QProduct.product.brand, QBrand.brand)
                .leftJoin(QReview.review).on(QReview.review.product.eq(QProduct.product))
                .where(QProduct.product.createdAt.month().eq(month))
                .groupBy(
                        QProduct.product.id,
                        QProduct.product.imageUrl,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.createdAt,
                        QProduct.product.brand.id,
                        QProduct.product.brand.name
                )
                .fetch();
    }
}
