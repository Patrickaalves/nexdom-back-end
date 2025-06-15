package com.nexdom.inventorycontrol.specifications;

import com.nexdom.inventorycontrol.model.ProductModel;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationProduct {
    @And({
            @Spec(path = "code", spec = LikeIgnoreCase.class),
            @Spec(path = "productType", spec = LikeIgnoreCase.class),
    })
    public interface ProductSpec extends Specification<ProductModel> {}
}
