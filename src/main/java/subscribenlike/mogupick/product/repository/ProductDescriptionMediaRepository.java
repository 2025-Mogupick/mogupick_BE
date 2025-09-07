package subscribenlike.mogupick.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductDescriptionMedia;
import subscribenlike.mogupick.product.domain.ProductMedia;

import java.util.List;

public interface ProductDescriptionMediaRepository extends JpaRepository<ProductDescriptionMedia, Long> {

    List<ProductMedia> findByProduct(Product product);

    List<ProductMedia> findByProductId(Long productId);

    void deleteByProduct(Product product);

    @Query("SELECT pm.imageUrl FROM ProductMedia pm WHERE pm.product.id = :productId ORDER BY pm.createdAt ASC")
    List<String> findImageUrlsByProductId(@Param("productId") Long productId);

    @Query("SELECT pm.imageUrl FROM ProductMedia pm WHERE pm.product IN :products ORDER BY pm.product.id, pm.createdAt ASC")
    List<String> findImageUrlsByProducts(@Param("products") List<Product> products);

    @Query("SELECT pm.imageUrl FROM ProductMedia pm WHERE pm.product.id = :productId ORDER BY pm.createdAt ASC LIMIT 1")
    String findFirstImageUrlByProductId(@Param("productId") Long productId);

    @Query(value = """
        SELECT pdm.product_id, pdm.image_url
        FROM product_description_media pdm
        INNER JOIN (
            SELECT product_id, MIN(created_at) as min_created_at
            FROM product_description_media
            WHERE product_id IN :productIds
            GROUP BY product_id
        ) first_images ON pdm.product_id = first_images.product_id
        AND pdm.created_at = first_images.min_created_at
        WHERE pdm.product_id IN :productIds
        """, nativeQuery = true)
    List<Object[]> findFirstImageUrlsByProductIds(@Param("productIds") List<Long> productIds);
}
