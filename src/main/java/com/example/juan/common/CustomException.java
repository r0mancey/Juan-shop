package com.example.juan.common;

/**
 * 自定义异常
 * 删除关联了商品的分类时抛出异常
 */
public class CustomException extends RuntimeException {
    public CustomException(String msg){
        super(msg);
    }
}
