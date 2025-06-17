package com.nexdom.inventorycontrol.specifications;

import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class SpecificationStockMovement {
    @And({
            @Spec(path = "operationType", spec = Equal.class),
            @Spec(path = "salePrice", params = "salePrice", spec = Equal.class),
            @Spec(path = "salePrice", params = "minSalePrice", spec = GreaterThanOrEqual.class),
            @Spec(path = "salePrice", params = "maxSalePrice", spec = LessThanOrEqual.class),
            @Spec(path = "saleDate", params = {"start", "end"}, spec = Between.class),
            @Spec(path = "movementQuantity", params = "movementQuantity", spec = Equal.class),
            @Spec(path = "movementQuantity", params = "minQuantity", spec = GreaterThanOrEqual.class),
            @Spec(path = "movementQuantity", params = "maxQuantity", spec = LessThanOrEqual.class),
    })
    public interface StockMovementSpec extends Specification<StockMovementModel> {}

    public static Specification<StockMovementModel> productStockMovements(final UUID productId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ProductModel> product = query.from(ProductModel.class);
            Expression<Collection<StockMovementModel>> productsInStockMovements = product.get("stockMovement");
            return cb.and(
                    cb.equal(product.get("productId"), productId),
                    cb.isMember(root, productsInStockMovements)
            );
        };
    }
}
