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
        return lock(resourceName, worker, 100);
    }

    @Override
    public Object lock(String resourceName, AquiredLockWorker worker, int lockTime) throws UnableToAquireLockException, Exception {
        RedissonClient client = redissonConnector.getClient();
        RLock lock = client.getLock(LOCAK_PREFIX + resourceName);
        //Wait for 100 seconds seconds and automatically unlock it after lockTime seconds
        boolean success = lock.tryLock(100, lockTime, TimeUnit.SECONDS);
        if (success){
            try {
                return worker.invokeAfterLockAquire();
            } finally {
                lock.unlock();
            }
        }
        throw new UnableToAquireLockException();
    }
}
