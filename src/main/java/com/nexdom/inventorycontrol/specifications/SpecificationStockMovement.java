package com.nexdom.inventorycontrol.specifications;

import com.nexdom.inventorycontrol.model.StockMovementModel;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

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
}
