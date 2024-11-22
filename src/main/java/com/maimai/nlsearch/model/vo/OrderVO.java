package com.maimai.nlsearch.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private Double remainingBalance;
    private String shippingType;
    private String orderStatus;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
}
