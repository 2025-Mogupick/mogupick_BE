package subscribenlike.mogupick.product.model;


public record FetchReviewResponse(
        Double rating,
        Long reviewCount) {
    public static FetchReviewResponse of(Double rating, Long reviewCount) {
        if(rating == null) {
            rating = 0.0;
        }

        if(reviewCount == null) {
            reviewCount = 0L;
        }

        return new FetchReviewResponse(rating, reviewCount);
    }
}
