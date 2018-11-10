package com.maxus.redisson.redlock;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 获取锁后需要处理的业务逻辑
 * Time:Create on 2018/11/10 13:40
 */
public interface AquiredLockWorker<T> {

    T invokeAfterLockAquire() throws Exception;
}
