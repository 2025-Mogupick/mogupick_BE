package subscribenlike.mogupick.product;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.product.domain.Product;

public enum ProductFixture {
    쿠팡구독("쿠팡 구독 서비스", "쿠팡의 다양한 구독 서비스를 제공합니다", "한국", 29900, "https://example.com/coupang-subscription.jpg"),
    네이버구독("네이버 구독 서비스", "네이버의 프리미엄 구독 서비스를 제공합니다", "한국", 19900, "https://example.com/naver-subscription.jpg"),
    카카오구독("카카오 구독 서비스", "카카오의 다양한 디지털 서비스를 제공합니다", "한국", 15900, "https://example.com/kakao-subscription.jpg"),
    모구픽구독("모구픽 구독 서비스", "모구픽의 맞춤형 구독 서비스를 제공합니다", "한국", 39900, "https://example.com/mogupick-subscription.jpg");

    private final String name;
    private final String description;
    private final String origin;
    private final int price;
    private final String imageUrl;

    ProductFixture(String name, String description, String origin, int price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product 쿠팡구독(Brand brand) {
        return new Product(쿠팡구독.name, 쿠팡구독.description, 쿠팡구독.origin, 쿠팡구독.price, 쿠팡구독.imageUrl, brand);
    }

    public static Product 네이버구독(Brand brand) {
        return new Product(네이버구독.name, 네이버구독.description, 네이버구독.origin, 네이버구독.price, 네이버구독.imageUrl, brand);
    }

    public static Product 카카오구독(Brand brand) {
        return new Product(카카오구독.name, 카카오구독.description, 카카오구독.origin, 카카오구독.price, 카카오구독.imageUrl, brand);
    }

    public static Product 모구픽구독(Brand brand) {
        return new Product(모구픽구독.name, 모구픽구독.description, 모구픽구독.origin, 모구픽구독.price, 모구픽구독.imageUrl, brand);
    }
}
