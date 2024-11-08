package com.maimai.nlsearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class OrderQuery {
    private double remainingBalance;
    private String shippingType;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
}
