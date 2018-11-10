package com.maxus.redisson.redlock;

import com.maxus.redisson.exception.UnableToAquireLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * Author: wymiracle
 * Description:
 * Time:Create on 2018/11/10 13:50
 */
@Component
public class RedisLocker implements DistributedLocker {

    private final static String LOCAK_PREFIX = "lock:";

    @Autowired
    RedissonConnector redissonConnector;

    @Override
    public Object lock(String resourceName, AquiredLockWorker worker) throws UnableToAquireLockException, Exception {
        return lock(resourceName, worker, 60);
    }

    @Override
    public Object lock(String resourceName, AquiredLockWorker worker, int lockTime) throws UnableToAquireLockException, Exception {
        RedissonClient client = redissonConnector.getClient();
        RLock lock = client.getLock(LOCAK_PREFIX + resourceName);
        //60表示等待时间，60秒内获取不到锁，直接返回;lockTime结束强制解锁，防止死锁
        boolean success = lock.tryLock(120, lockTime, TimeUnit.SECONDS);
        if (success){
            try {
                //执行业务逻辑
                return worker.invokeAfterLockAquire();
            } finally {
                //解锁
                lock.unlock();
            }
        }
        throw new UnableToAquireLockException();
    }
}
