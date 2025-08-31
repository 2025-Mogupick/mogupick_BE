package subscribenlike.mogupick.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;

@Getter
@AllArgsConstructor(staticName = "of")
public class ProductWithOptionResponse {
    private Product product;
    private ProductOption option;
}
