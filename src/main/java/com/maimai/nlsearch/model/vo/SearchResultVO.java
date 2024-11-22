package com.maimai.nlsearch.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultVO<T> {
    private String resultType;
    private List<T> resultList;
}
