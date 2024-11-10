package com.maimai.nlsearch.service;

import com.maimai.nlsearch.model.dto.SearchQueryDTO;
import com.maimai.nlsearch.model.entity.Customer;
import com.maimai.nlsearch.model.entity.Order;
import com.maimai.nlsearch.model.vo.CustomerVO;
import com.maimai.nlsearch.model.vo.OrderVO;
import com.maimai.nlsearch.model.vo.SearchResultVO;
import com.maimai.nlsearch.repositories.CustomerRepository;
import com.maimai.nlsearch.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ChatClient chatClient;


    public SearchResultVO<?> getSearchResult(SearchQueryDTO searchQueryDTO) {
        String searchQuery = searchQueryDTO.getSearchQuery();
        String prompt = """
                Given a natural language query, extract relevant fields and values to query customer and order information. Return a JSON response indicating the database name and field-value pairs.

                Available fields:
                Customers table: id, firstName, lastName, address, city, note
                Orders table: orderItemId, customerId, totalAmount, orderStatus, shippingType, orderDate, paymentDate, deliverDate
                        
                Constraints:
                1. shippingType only has 4 enums: EXPRESS("express"), STANDARD("standard"), PICKUP("pickup"), OVERNIGHT("overnight");
                2. orderStatus also has 5 enums:     DELIVERED("delivered"), PROCESSING("processing"), SHIPPED("shipped"), PENDING("pending"), CANCELLED("cancelled"),

                You will only have 4 options to response:
                1. The related response:
                    Example input: "give me all customers which first name is Emma, and located in Toronto"
                    Example output: {"first_name": "Emma", "city": "Calgary", "table": "customers"}
                2. Return "Error: Current query is not related to searching" if the query is not related to this stuff
                3. Return "Error: We do not support search for [field_name]" if the query is using fields not in the Available fields list
                4. Return "Error: [your error message]" if there is any other error, and please fill in your error message, within 30 words
                        
                Convert the following query into the same JSON format: %s
                """;
        String actualPrompt = String.format(prompt, searchQuery);

        // parse the msg into a json String, then to a map
        String jsonStringFromAI= chatClient.prompt()
                .user(actualPrompt)
                .call().content();
        if (jsonStringFromAI.startsWith("Error")) log.info(jsonStringFromAI);
        JSONObject jsonObject = new JSONObject(jsonStringFromAI);
        Map<String, Object> map = jsonObject.toMap();

        // if the value of "database" is "customers" or "orders", init the variable
        String tableType = (String) map.get("table");
        if ("customers".equals(tableType)) {
            SearchResultVO<CustomerVO> customerResultVO = new SearchResultVO<>();
            customerResultVO.setResultList(searchCustomersTable(map));
            return customerResultVO;
        } else {
            SearchResultVO<OrderVO> orderResultVO = new SearchResultVO<>();
            orderResultVO.setResultList(searchOrdersTable(map));
            return orderResultVO;
        }

    }

    private List<CustomerVO> searchCustomersTable(Map<String, Object> map) {
        List<CustomerVO> resultVO = new ArrayList<>();

        String firstName = (String) map.getOrDefault("first_name", null);
        String lastName = (String) map.getOrDefault("lastName", null);
        String city = (String) map.getOrDefault("city", null);
        String note = (String) map.getOrDefault("note", null);


        List<Customer> customers = customerRepository.searchCustomers(city, firstName, lastName, note);
        for (Customer customer: customers) {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            resultVO.add(customerVO);
        }
        return resultVO;
    }

    private List<OrderVO> searchOrdersTable(Map<String, Object> map) {
        List<OrderVO> resultVO = new ArrayList<>();

        List<Order> orders = orderRepository.searchOrders(0.00, "express", "delivered", null, null, null);

        for (Order order: orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            resultVO.add(orderVO);
        }
        return resultVO;
    }
}