package com.maimai.nlsearch.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultVO<T> {
    private List<T> resultList;
}
