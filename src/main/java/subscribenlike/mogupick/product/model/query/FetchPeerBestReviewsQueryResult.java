package subscribenlike.mogupick.product.model.query;

import java.time.LocalDateTime;


public interface FetchPeerBestReviewsQueryResult {
    Long getProductId();
    String getBrandName();
    String getProductName();
    Integer getPrice();
    Integer getMemberBirthYear();
    String getMemberProfileImageUrl();
    String getMemberName();
    String getReviewImageUrl();
    LocalDateTime getReviewCreatedAt();
    Long getLikeCount();
    Long getReviewCount();
}
