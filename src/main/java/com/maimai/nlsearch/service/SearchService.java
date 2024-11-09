package com.maimai.nlsearch.service;

import com.maimai.nlsearch.common.enums.ShippingType;
import com.maimai.nlsearch.model.dto.SearchQueryDTO;
import com.maimai.nlsearch.model.entity.Customer;
import com.maimai.nlsearch.model.entity.Order;
import com.maimai.nlsearch.model.vo.CustomerVO;
import com.maimai.nlsearch.model.vo.OrderVO;
import com.maimai.nlsearch.model.vo.SearchResultVO;
import com.maimai.nlsearch.repositories.CustomerRepository;
import com.maimai.nlsearch.repositories.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public String test() {
        return "testing";
    }

    public SearchResultVO getSearchResult(SearchQueryDTO searchQueryDTO) {
        // two new list for return VO
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<OrderVO> orderVOList = new ArrayList<>();

        String searchQuery = searchQueryDTO.getSearchQuery();

        // parse the msg into Array of queries?

        // run query in queries in repository, loop


        // customers table only support search for 4 fields: first name, last name, city, note
        List<Customer> customers = customerRepository.searchCustomers(null, "Emma", null, null);
        for (Customer customer: customers) {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            customerVOList.add(customerVO);
        }

        List<Order> orders = orderRepository.searchOrders(0.00, "express", "delivered", null, null, null);
        for (Order order: orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOList.add(orderVO);
        }


        SearchResultVO searchResultVO = new SearchResultVO();
        searchResultVO.setCustomerVOList(customerVOList);
        searchResultVO.setOrderVOList(orderVOList);
        return searchResultVO;
    }
}