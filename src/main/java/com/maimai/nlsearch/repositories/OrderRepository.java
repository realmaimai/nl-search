package com.maimai.nlsearch.repositories;

import com.maimai.nlsearch.common.enums.OrderStatus;
import com.maimai.nlsearch.common.enums.ShippingType;
import com.maimai.nlsearch.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
        SELECT * FROM orders 
        WHERE (:remainingBalance IS NULL OR remaining_balance = :remainingBalance)
        AND (:shippingType IS NULL OR shipping_type LIKE CONCAT('%', :shippingType, '%'))
        AND (:status IS NULL OR status LIKE CONCAT('%', :status, '%'))
        AND (:orderDate IS NULL OR DATE(order_date) = DATE(:orderDate))
        AND (:paymentDate IS NULL OR DATE(payment_date) = DATE(:paymentDate))
        AND (:deliverDate IS NULL OR DATE(deliver_date) = DATE(:deliverDate))
        """, nativeQuery = true)
    List<Order> searchOrders(
            @Param("remainingBalance") Double remainingBalance,
            @Param("shippingType") ShippingType shippingType,
            @Param("status") OrderStatus status,
            @Param("orderDate") LocalDateTime orderDate,
            @Param("paymentDate") LocalDateTime paymentDate,
            @Param("deliverDate") LocalDateTime deliverDate
    );
}
