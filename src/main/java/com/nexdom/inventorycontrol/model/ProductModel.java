package com.nexdom.inventorycontrol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.exceptions.BusinessInsuficientStock;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonIgnore
    private UUID productId;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal supplierPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<StockMovementModel> stockMovement;

    @Version                          // <‑ lock otimista
    private Long version;

    public void creditStock(int qty) {
        validatePositive(qty);
        stockQuantity += qty;
    }

    public void debitStock(int qty) {
        validatePositive(qty);
        if (stockQuantity < qty) {
            throw new BusinessInsuficientStock("Insufficient stock: requested " + qty + ", current stock " + stockQuantity);
        }
        stockQuantity -= qty;
    }

    private void validatePositive(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(BigDecimal supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<StockMovementModel> getStockMovement() {
        return stockMovement;
    }

    public void setStockMovement(Set<StockMovementModel> stockMovement) {
        this.stockMovement = stockMovement;
    }
}
