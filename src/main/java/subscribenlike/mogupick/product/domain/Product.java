package subscribenlike.mogupick.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String origin;

    private int price;

    @ManyToOne
    private Brand brand;

    public Product(String name, String description, String origin, int price, Brand brand) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.price = price;
        this.brand = brand;
    }

    public String getBrandName(){
        return brand.getName();
    }
}
