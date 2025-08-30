package subscribenlike.mogupick.product.model;


public record FetchReviewResponse(
        double rating,
        int reviewCount) {
    public static FetchReviewResponse of(double rating, int reviewCount) {
        return new FetchReviewResponse(rating, reviewCount);
    }
}
