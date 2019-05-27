package org.openbpm.sys.simplemq.producer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.sys.api.jms.constants.JmsDestinationConstant;
import org.openbpm.sys.api.jms.model.JmsDTO;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Redis消息队列提供实现
 *
 *
 */
public class RedisMessageQueueProducer implements JmsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageQueueProducer.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(JmsDTO message) {
        if (message == null) {
            LOGGER.info("传入参数为空, 跳过执行");
            return;
        }
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPush(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            LOGGER.info("传入参数为空, 跳过执行");
            return;
        }
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPushAll(messages.toArray());
    }
}