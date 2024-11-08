package com.maimai.nlsearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CustomerQuery {
    private String firstName;
    private String lastName;
    private String city;
    private String note;
}
