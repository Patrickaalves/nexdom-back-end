package com.nexdom.inventorycontrol.specifications;

import com.nexdom.inventorycontrol.model.CustomerModel;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationCustomer {
    @And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
    })
    public interface CustomerSpec extends Specification<CustomerModel> {}

}
