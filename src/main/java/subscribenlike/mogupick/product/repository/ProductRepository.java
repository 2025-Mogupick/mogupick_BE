package subscribenlike.mogupick.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.query.FetchPeerBestReviewsQueryResult;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQuerydslRepository {

    List<Product> findAllByIdIn(List<Long> productIds);

    default Map<Long, Product> findAllByIdInMaps(List<Long> productIds) {
        return findAllByIdIn(productIds).stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, product -> product));
    }

    default Product getById(Long productId) {
        return findById(productId).orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    List<Product> findByNameContainingIgnoreCase(String keyword);

    @Query(value = """
        SELECT
            p.id AS productId,
            b.name AS brandName,
            p.name AS productName,
            p.price AS price,
            YEAR(m.birth_date) AS birth_year,
            m.profile_image AS profileImage,
            m.name AS memberName,
            r.review_image AS reviewImage,
            r.created_at AS createdAt,
            COALESCE(review_likes.review_like, 0) AS likeCount,
            COALESCE(review_counts.review_count, 0) AS reviewCount
        FROM review r
        JOIN member m ON r.member_id = m.id
        JOIN product p ON r.product_id = p.id
        JOIN brand b ON b.id = p.brand_id
        LEFT JOIN (
            SELECT
                review_likes.review_id,
                COUNT(*) AS review_like
            FROM review_likes
            GROUP BY review_id
        ) AS review_likes ON r.id = review_likes.review_id
        LEFT JOIN (
            SELECT
                review.product_id,
                COUNT(*) AS review_count
            FROM review
            GROUP BY product_id
        ) AS review_counts ON p.id = review_counts.product_id
        WHERE YEAR(m.birth_date) BETWEEN :fromYear AND :toYear
        ORDER BY likeCount DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<FetchPeerBestReviewsQueryResult> fetchPeerBestReviewNative(
            @Param("fromYear") int fromYear,
            @Param("toYear") int toYear,
            @Param("limit") int limit
    );
}
