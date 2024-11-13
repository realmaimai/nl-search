package com.maimai.nlsearch.repositories;

import com.maimai.nlsearch.common.enums.OrderStatus;
import com.maimai.nlsearch.common.enums.ShippingType;
import com.maimai.nlsearch.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
        SELECT * FROM orders WHERE 
        (:remainingBalance IS NULL OR remaining_balance = :remainingBalance) 
        AND (:shippingType IS NULL OR shipping_type LIKE CONCAT('%', :shippingType, '%'))
        AND (:orderStatus IS NULL OR order_status LIKE CONCAT('%', :orderStatus, '%'))
        AND (:orderStartDate IS NULL OR DATE(order_date) BETWEEN DATE(:orderStartDate) AND DATE(:orderEndDate))
        AND (:paymentStartDate IS NULL OR DATE(payment_date) BETWEEN DATE(:paymentStartDate) AND DATE(:paymentEndDate))
        AND (:deliverStartDate IS NULL OR DATE(deliver_date) BETWEEN DATE(:deliverStartDate) AND DATE(:deliverEndDate))
    """, nativeQuery = true)
    List<Order> searchOrders(
            @Param("remainingBalance") Double remainingBalance,
            @Param("shippingType") String shippingType,
            @Param("orderStatus") String orderStatus,
            @Param("orderStartDate") LocalDate orderStartDate,
            @Param("orderEndDate") LocalDate orderEndDate,
            @Param("paymentStartDate") LocalDate paymentStartDate,
            @Param("paymentEndDate") LocalDate paymentEndDate,
            @Param("deliverStartDate") LocalDate deliverStartDate,
            @Param("deliverEndDate") LocalDate deliverEndDate
    );
}
