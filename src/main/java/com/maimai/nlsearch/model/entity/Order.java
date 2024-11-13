package com.maimai.nlsearch.model.entity;

import com.maimai.nlsearch.common.enums.OrderStatus;
import com.maimai.nlsearch.common.enums.ShippingType;
import com.maimai.nlsearch.converter.OrderStatusConverter;
import com.maimai.nlsearch.converter.ShippingTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_item_id", nullable = false)
    private String orderItemId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal paidBalance;

    @Column(name = "remaining_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal remainingBalance;

    @Column(name = "shipping_type", nullable = false)
    @Convert(converter =  ShippingTypeConverter.class)
    private ShippingType shippingType;

    @Column(name = "order_status", nullable = false)
    @Convert(converter =  OrderStatusConverter.class)
    private OrderStatus orderStatus;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "deliver_date")
    private LocalDateTime deliverDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
