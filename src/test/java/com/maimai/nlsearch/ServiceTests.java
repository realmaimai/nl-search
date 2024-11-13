package com.maimai.nlsearch;

import com.maimai.nlsearch.model.dto.SearchQueryDTO;
import com.maimai.nlsearch.model.entity.Order;
import com.maimai.nlsearch.model.vo.SearchResultVO;
import com.maimai.nlsearch.repositories.OrderRepository;
import com.maimai.nlsearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@SpringBootTest
public class ServiceTests {

    @Autowired
    private SearchService searchService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void getCustomerResultFromQuery() {
        SearchQueryDTO searchQueryDTO = new SearchQueryDTO();
        searchQueryDTO.setSearchQuery("give me all customers whose first name is Emma, and located in Vancouver");
        SearchResultVO<?> searchResult = searchService.getSearchResult(searchQueryDTO);
        List<?> resultList = searchResult.getResultList();
        log.info("result list: " + resultList);
    }

    @Test
    public void getOrderResultFromQuery() {
        SearchQueryDTO searchQueryDTO = new SearchQueryDTO();
        searchQueryDTO.setSearchQuery("give me all orders where the remaining balance is 0, and the order start date should be at 2024 January 10th");
        SearchResultVO<?> searchResult = searchService.getSearchResult(searchQueryDTO);
        List<?> resultList = searchResult.getResultList();
        log.info("result list: " + resultList);
    }

    @Test
    public void getOrderResultFromRepository() {

        LocalDate orderStartDate = LocalDate.parse("2024-01-10");
        LocalDate orderEndDate = LocalDate.parse("2024-03-10");
        List<Order> orders = orderRepository.searchOrders(
                0.0,
                null,
                null,
                orderStartDate,
                orderEndDate,
                null,
                null,
                null,
                null
        );

        log.info("-- Start printing order info --");
        for (Order order : orders) log.info(order.getOrderItemId());
    }
}
