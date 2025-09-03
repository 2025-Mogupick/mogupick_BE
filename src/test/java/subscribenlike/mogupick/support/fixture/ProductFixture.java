package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.product.domain.Product;

import java.math.BigDecimal;

public enum ProductFixture {
    구독상품1("구독 상품 1", BigDecimal.valueOf(10000), "product1.jpg"),
    구독상품2("구독 상품 2", BigDecimal.valueOf(20000), "product2.jpg"),
    구독상품3("구독 상품 3", BigDecimal.valueOf(15000), "product3.jpg");
    
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    
    ProductFixture(String productName, BigDecimal price, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    
    public static Product 구독상품1(Brand brand) {
        return new Product(
                구독상품1.productName,
                "테스트 상품 설명 1",
                "한국",
                구독상품1.price.intValue(),
                구독상품1.imageUrl,
                brand
        );
    }
    
    public static Product 구독상품2(Brand brand) {
        return new Product(
                구독상품2.productName,
                "테스트 상품 설명 2",
                "한국",
                구독상품2.price.intValue(),
                구독상품2.imageUrl,
                brand
        );
    }
    
    public static Product 구독상품3(Brand brand) {
        return new Product(
                구독상품3.productName,
                "테스트 상품 설명 3",
                "한국",
                구독상품3.price.intValue(),
                구독상품3.imageUrl,
                brand
        );
    }
}
