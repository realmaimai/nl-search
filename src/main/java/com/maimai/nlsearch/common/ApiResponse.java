package com.maimai.nlsearch.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> success(String msg, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("200");
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(String code, String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
