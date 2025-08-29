package subscribenlike.mogupick.product;


import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;

import java.util.List;

public interface ProductQuerydslRepository {
    List<ProductsInMonthQueryResult> findAllProductsInMonth(int month);
}
