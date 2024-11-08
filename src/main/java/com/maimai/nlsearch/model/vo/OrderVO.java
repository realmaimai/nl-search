package com.maimai.nlsearch.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {
    private String orderItemId;
    private String customerId;
    private Integer totalAmount;
    private Integer shippingType;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
}
