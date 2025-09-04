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
            YEAR(m.birth_date) AS memberBirthYear,
            m.profile_image AS memberProfileImageUrl,
            m.name AS memberName,
            r.created_at AS reviewCreatedAt,
            AVG(r.score) AS reviewScore,
            COALESCE(rl.review_like, 0) AS likeCount,
            COALESCE(rc.review_count, 0) AS reviewCount,
            pi.image_url AS productImageUrl,
            ri.image_url AS reviewImageUrl
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
        ) AS rl ON r.id = rl.review_id
        LEFT JOIN (
            SELECT
                review.product_id,
                COUNT(*) AS review_count
            FROM review
            GROUP BY product_id
        ) AS rc ON p.id = rc.product_id
        LEFT JOIN (
            SELECT pm.product_id, pm.image_url
            FROM product_media pm
            INNER JOIN (
                SELECT product_id, MIN(created_at) as min_created_at
                FROM product_media
                GROUP BY product_id
            ) first_images ON pm.product_id = first_images.product_id
            AND pm.created_at = first_images.min_created_at
        ) AS pi ON p.id = pi.product_id
        LEFT JOIN (
            SELECT rm.review_id, rm.image_url
            FROM review_media rm
            INNER JOIN (
                SELECT review_id, MIN(created_at) as min_created_at
                FROM review_media
                GROUP BY review_id
            ) first_review_images ON rm.review_id = first_review_images.review_id
            AND rm.created_at = first_review_images.min_created_at
        ) AS ri ON r.id = ri.review_id
        WHERE YEAR(m.birth_date) BETWEEN :fromYear AND :toYear
        GROUP BY(
            p.id, b.name, p.name, p.price, m.birth_date,
            m.profile_image, m.name, r.created_at,
            rl.review_like, rc.review_count, pi.image_url, ri.image_url)
        ORDER BY likeCount DESC
        LIMIT :limit
    """,
            nativeQuery = true
    )
    List<FetchPeerBestReviewsQueryResult> fetchPeerBestReviewNative(
            @Param("fromYear") int fromYear,
            @Param("toYear") int toYear,
            @Param("limit") int limit
    );
}
