package org.openbpm.bpm.sys.autoconfiguration;

import org.openbpm.sys.api.jms.constants.JmsDestinationConstant;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.openbpm.sys.simplemq.consumer.CommonMessageQueueConsumer;
import org.openbpm.sys.simplemq.producer.CommonMessageQueueProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.ConnectionFactory;

/**
 * 通用消息队列自动装配
 *
 *
 */
@ConditionalOnExpression("'${ab.simple-mq.message-queue-type}'.toLowerCase() == 'jms'")
@Configuration
@EnableJms
public class AbMessageQueueAutoConfiguration {

    @Bean
    public JmsProducer jmsProducer() {

        return new CommonMessageQueueProducer();
    }

    @Bean
    public CommonMessageQueueConsumer messageQueueConsumer() {

        return new CommonMessageQueueConsumer();
    }

    @Bean
    public MessageListenerAdapter commonMessageQueueConsumerListenerAdapter(CommonMessageQueueConsumer messageQueueConsumer) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDelegate(messageQueueConsumer);
        return messageListenerAdapter;
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                                    @Qualifier("commonMessageQueueConsumerListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestinationName(JmsDestinationConstant.DEFAULT_NAME);
        messageListenerContainer.setMessageListener(messageListenerAdapter);
        messageListenerContainer.setConnectionFactory(connectionFactory);
        return messageListenerContainer;
    }


}
