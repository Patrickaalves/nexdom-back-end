package com.nexdom.inventorycontrol.specifications;

import com.nexdom.inventorycontrol.model.SupplierModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationSupplier {
    @And({
            @Spec(path = "code", spec = Equal.class),
    })
    public interface SupplierSpec extends Specification<SupplierModel> {}
}

