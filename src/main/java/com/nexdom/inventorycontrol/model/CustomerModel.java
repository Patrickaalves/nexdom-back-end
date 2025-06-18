package com.nexdom.inventorycontrol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_CUSTOMERS")
public class CustomerModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true)
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<StockMovementModel> stockMovement;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<StockMovementModel> getStockMovement() {
        return stockMovement;
    }

    public void setStockMovement(Set<StockMovementModel> stockMovement) {
        this.stockMovement = stockMovement;
    }
}
