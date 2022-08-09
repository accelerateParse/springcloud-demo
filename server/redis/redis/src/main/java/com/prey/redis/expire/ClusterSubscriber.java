package com.prey.redis.expire;


import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubAdapter;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.cluster.pubsub.api.async.NodeSelectionPubSubAsyncCommands;
import io.lettuce.core.cluster.pubsub.api.async.PubSubAsyncNodeSelection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * 绑定监听事件
 *
 * @author 程就人生
 * @date 2021年2月1日
 * @Description
 */
@SuppressWarnings("rawtypes")
@Component
public class ClusterSubscriber extends RedisPubSubAdapter implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(ClusterSubscriber.class);

    //过期事件监听
    private static final String EXPIRED_CHANNEL = "__keyevent@0__:expired";

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.password}")
    private String password;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("过期事件，启动监听......");
        //项目启动后就运行该方法
        startListener();
    }

    /**
     * 启动监听
     */
    @SuppressWarnings("unchecked")
    public void startListener() {
        //redis集群监听
        String[] redisNodes = clusterNodes.split(",");
        //监听其中一个端口号即可
        RedisURI redisURI = RedisURI.create("redis://" + redisNodes[0]);
        redisURI.setPassword(password);
        RedisClusterClient clusterClient = RedisClusterClient.create(redisURI);

        StatefulRedisClusterPubSubConnection<String, String> pubSubConnection = clusterClient.connectPubSub();
        //redis节点间消息的传播为true
        pubSubConnection.setNodeMessagePropagation(true);
        //过期消息的接受和处理
        pubSubConnection.addListener(new RedisClusterPubSubAdapter() {
            @Override
            public void message(RedisClusterNode node, Object channel, Object message) {
                String msg = message.toString();
                try {

                } catch (Exception e) {
                    log.error(e.getMessage() + "--- error from key : " + message);
                }
            }
        });

        //异步操作
        PubSubAsyncNodeSelection<String, String> masters = pubSubConnection.async().masters();
        NodeSelectionPubSubAsyncCommands<String, String> commands = masters.commands();
        //设置订阅消息类型，一个或多个
        commands.subscribe(EXPIRED_CHANNEL);
    }





}