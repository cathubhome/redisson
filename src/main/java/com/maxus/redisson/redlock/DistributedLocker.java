package com.maxus.redisson.redlock;

import com.maxus.redisson.exception.UnableToAquireLockException;

/**
 * Created with IDEA
 * Author: wymiracle
 * Description: 获取锁管理类
 * Time:Create on 2018/11/10 13:43
 */
public interface DistributedLocker<T> {

    /**
     * 获取锁
     * @param resourceName 锁的名称
     * @param worker 获取锁后的处理类
     * @param <T>
     * @return 处理完具体的业务逻辑要返回的数据
     * @throws UnableToAquireLockException
     * @throws Exception
     */
    <T> T lock(String resourceName, AquiredLockWorker<T> worker) throws UnableToAquireLockException, Exception;

    <T> T lock(String resourceName, AquiredLockWorker<T> worker, int lockTime) throws UnableToAquireLockException, Exception;
}
