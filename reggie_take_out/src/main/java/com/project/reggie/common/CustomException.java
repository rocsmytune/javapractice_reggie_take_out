package com.project.reggie.common;

/**
 * self defined exception class
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg) {
        super(msg);
    }
}
