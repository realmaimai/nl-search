package com.maimai.nlsearch.util;

import java.time.LocalDate;
import java.util.Map;

public class ParseUtil {

    public static LocalDate getDateFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null && key.contains("start")) {
            return LocalDate.parse("1900-01-01");
        } else if (value == null && key.contains("end")) {
            return LocalDate.now();
        }

        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }

        if (value == null) { return null;}
        return LocalDate.parse(value.toString());
    }
}
