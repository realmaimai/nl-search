package com.maimai.nlsearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ParseTests {

    @Autowired
    private ChatClient chatClient;

    @Test
    public void getResponse() {
        String content = chatClient.prompt()
                .user("give me a random json, within 20 characters")
                .call()
                .content();
        log.info("result: " + content);

    }

    @Test
    public void getProperties() {
        ChatResponse chatResponse = chatClient.prompt().user("give me only one English name within 10 characters")
                .call().chatResponse();
        ChatResponseMetadata metadata = chatResponse.getMetadata();
        Generation result = chatResponse.getResult();
        log.info(metadata.toString());
        log.info("===");
        log.info(result.toString());
    }

    @Test
    public void getResponseWithQuery() {
        String searchQuery = "give me all first name is Emma, and located in Calgary";
        String prompt = """
        Given a natural language query, extract relevant fields and values to query customer and order information. Return a JSON response indicating the database name and field-value pairs.

        Available fields:
        Customers: id, firstName, lastName, address, city, note
        Orders: orderItemId, customerId, totalAmount, shippingType, orderDate, paymentDate, deliverDate

        Example input: "give me all first name is Emma, and located in Toronto"
        Example output: {"first_name": "Emma", "city": "Calgary", "table": "customers"}

        Convert the following query into the same JSON format: %s
        """;
        String actualPrompt = String.format(prompt, searchQuery);
        String content = chatClient.prompt()
                .user(actualPrompt)
                .call().content();
        log.info("result from ai: "+ content);
    }
}
