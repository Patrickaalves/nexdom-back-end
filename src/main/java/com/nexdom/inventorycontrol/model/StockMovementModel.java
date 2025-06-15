package com.nexdom.inventorycontrol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nexdom.inventorycontrol.enums.OperationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_STOCK_MOVEMENTS")
public class StockMovementModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonIgnore
    private UUID stockMovementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "product_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_stock_product",
                    foreignKeyDefinition =
                            "FOREIGN KEY (product_id) REFERENCES tb_products(product_id) ON DELETE SET NULL"))
    private ProductModel product;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private Integer movementQuantity;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(nullable = false, length = 20)
    private String productCode;

    @PrePersist
    public void prePersist() {
        if (this.product != null) {
            this.productCode = product.getCode();
        }
    }

    public UUID getStockMovementId() {
        return stockMovementId;
    }

    public void setStockMovementId(UUID stockMovementId) {
        this.stockMovementId = stockMovementId;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getMovementQuantity() {
        return movementQuantity;
    }

    public void setMovementQuantity(Integer movementQuantity) {
        this.movementQuantity = movementQuantity;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
