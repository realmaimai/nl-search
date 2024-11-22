package com.maimai.nlsearch.controller;

import com.maimai.nlsearch.common.ApiResponse;
import com.maimai.nlsearch.common.constants.ResponseMessage;
import com.maimai.nlsearch.model.dto.SearchQueryDTO;
import com.maimai.nlsearch.model.vo.SearchResultVO;
import com.maimai.nlsearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("/result")
    public ApiResponse<SearchResultVO> getByQuery(@RequestBody SearchQueryDTO searchQueryDTO) {
        SearchResultVO searchResult = searchService.getSearchResult(searchQueryDTO);
        return ApiResponse.success(ResponseMessage.SUCCESS, searchResult);
    }


}
