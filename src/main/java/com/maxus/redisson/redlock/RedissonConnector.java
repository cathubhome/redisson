package com.maxus.redisson.redlock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IDEA
 * Author: wymiracle
 * Description: 获取RedissonClient连接类
 * Time:Create on 2018/11/10 13:45
 */
@Component
public class RedissonConnector {

    RedissonClient redisson;

    @PostConstruct
    public void init() {
        Config config = new Config();
        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
        //之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
        //改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
        config.setCodec(new org.redisson.client.codec.StringCodec());
        //指定使用单节点部署方式
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("192.168.17.18:6379").setPassword("cathome");
        singleServerConfig.setConnectionPoolSize(500);//设置对于master节点的连接池中连接数最大为500
        singleServerConfig.setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        singleServerConfig.setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
        singleServerConfig.setTimeout(3000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
        singleServerConfig.setPingTimeout(30000);
        singleServerConfig.setReconnectionTimeout(3000);//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
        redisson = Redisson.create(config);
    }

    public RedissonClient getClient() {
        return redisson;
    }

}
