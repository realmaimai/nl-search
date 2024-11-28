package com.maimai.nlsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "search")
public class SearchConfig {
    private Map<String, List<String>> fields;
    private Map<String, List<String>> allowedValues;
}
