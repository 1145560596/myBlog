package com.atme.blog.utils;

import org.thymeleaf.util.StringUtils;

/**
 * 响应结果生成工具
 * @author shkstart
 * @create 2020-10-21-13:24
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int DEFAULT_CODE_SERVER_ERROR = 500;

    public static Result getSuccessResult() {
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS);
        return result;
    }

    public static Result getSuccessResult(String message) {
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static Result getSuccessResult(Object data) {
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_FAIL_MESSAGE);
        result.setData(data);
        return result;
    }

    public static Result getFailResult(String message) {
        Result result = new Result();
        result.setResultCode(DEFAULT_CODE_SERVER_ERROR);
        if(StringUtils.isEmpty(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static Result getErrorResult(int code,String message) {
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }

}
