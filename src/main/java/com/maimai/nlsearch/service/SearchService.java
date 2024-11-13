package com.maimai.nlsearch.service;

import com.maimai.nlsearch.model.dto.SearchQueryDTO;
import com.maimai.nlsearch.model.entity.Customer;
import com.maimai.nlsearch.model.entity.Order;
import com.maimai.nlsearch.model.vo.CustomerVO;
import com.maimai.nlsearch.model.vo.OrderVO;
import com.maimai.nlsearch.model.vo.SearchResultVO;
import com.maimai.nlsearch.repositories.CustomerRepository;
import com.maimai.nlsearch.repositories.OrderRepository;
import com.maimai.nlsearch.util.ParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.maimai.nlsearch.util.ParseUtil.getDateFromMap;

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
                Given a natural language query, extract relevant fields and values to query customer and order information. Return a JSON response indicating the database table and field-value pairs.
                                  
                Available fields:
                Customers table:
                - first_name
                - last_name
                - address
                - city
                - note
                                  
                Orders table:
                - remaining_balance (numeric)
                - order_status (string)
                - shipping_type (string)
                - order_start_date (date)
                - payment_start_date (date)
                - deliver_start_date (date)
                - order_end_date (date)
                - payment_end_date (date)
                - deliver_end_date (date)
                                  
                shipping_type:
                - "express"
                - "standard"
                - "pickup"
                - "overnight"
                                  
                order_status:
                - "delivered"
                - "processing"
                - "shipped"
                - "pending"
                - "cancelled"
                                  
                Response Format:
                1. For valid searches:
                {
                    "table": "[customers|orders]",
                    "field_name1": "value1",
                    "field_name2": "value2"
                    // ... all fields should be in snake_case
                }
                                  
                2. For invalid or non-search queries:
                "Error: Current query is not related to searching"
                                  
                3. For unsupported fields:
                "Error: We do not support search for [field_name]"
                                  
                4. For other errors:
                "Error: [specific error message within 30 words]"
                
                5. Only gives me Json, do not add anya other words
                                  
                Examples:
                Input: "find customers named Emma in Toronto"
                Output: {
                    "table": "customers",
                    "first_name": "Emma",
                    "city": "Toronto"
                }
                                  
                Input: "show me all express shipping orders"
                Output: {
                    "table": "orders",
                    "shipping_type": "express"
                }
                                  
                Input: "what's the weather today?"
                Output: "Error: Current query is not related to searching"
                                  
                Input: "find orders by email address"
                Output: "Error: We do not support search for email"
                                  
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

        // if the value of "database" is "customers" or "orders", create a new VO as result
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
        String lastName = (String) map.getOrDefault("last_name", null);
        String city = (String) map.getOrDefault("city", null);
        String note = (String) map.getOrDefault("note", null);


        List<Customer> customers = customerRepository.searchCustomers(city, firstName, lastName, note);
        for (Customer customer: customers) {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            customerVO.setId(String.valueOf(customer.getId()));
            resultVO.add(customerVO);
        }
        return resultVO;
    }

    private List<OrderVO> searchOrdersTable(Map<String, Object> map) {
        List<OrderVO> resultVO = new ArrayList<>();

        // getting the data from the map object
        Double remainingBalance = Double.parseDouble(map.getOrDefault("remaining_balance", null).toString());
        String shippingType = (String) map.getOrDefault("shipping_type", null);
        String orderStatus = (String) map.getOrDefault("order_status", null);
        LocalDate orderStartDate = getDateFromMap(map, "order_start_date");
        LocalDate orderEndDate = getDateFromMap(map, "order_end_date");
        LocalDate paymentStartDate = getDateFromMap(map, "payment_start_date");
        LocalDate paymentEndDate = getDateFromMap(map, "payment_end_date");
        LocalDate deliverStartDate = getDateFromMap(map, "deliver_start_date");
        LocalDate deliverEndDate = getDateFromMap(map, "deliver_end_date");

        List<Order> orders = orderRepository.searchOrders(
                remainingBalance,
                shippingType,
                orderStatus,
                orderStartDate,
                orderEndDate,
                paymentStartDate,
                paymentEndDate,
                deliverStartDate,
                deliverEndDate
        );

        for (Order order: orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVO.setRemainingBalance(order.getRemainingBalance().doubleValue());
            orderVO.setShippingType(order.getShippingType().getValue());
            orderVO.setOrderStatus(order.getOrderStatus().getValue());
            resultVO.add(orderVO);
        }

        return resultVO;
    }
}