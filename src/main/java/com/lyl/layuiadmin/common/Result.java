package com.lyl.layuiadmin.common;

import java.util.List;
/**
 * 通用响应体
 */
public class Result {
    private int code;
    private String msg;
    private long count;
    private List<?> data;

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public List<?> getData() {
        return data;
    }
    public void setData(List<?> data) {
        this.data = data;
    }
    
    public static Result build(Integer code, String msg, List<?> data) {
        return new Result(code, msg, data);
    }

    public static Result ok(List<?> data) {
        return new Result(data);
    }

    public static Result ok() {
        return new Result(null);
    }

    public Result() {

    }

    public static Result build(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public Result(Integer code, String msg, List<?> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(List<?> data) {
        this.code = 0;
        this.msg = "OK";
        this.data = data;
    }
}
