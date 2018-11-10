package com.maxus.redisson.exception;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 异常类
 * Time:Create on 2018/11/10 13:38
 */
public class UnableToAquireLockException extends RuntimeException {

    public UnableToAquireLockException() {
    }

    public UnableToAquireLockException(String message) {
        super(message);
    }

    public UnableToAquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
