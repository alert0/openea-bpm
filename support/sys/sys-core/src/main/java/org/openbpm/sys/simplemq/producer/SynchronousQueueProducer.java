package org.openbpm.sys.simplemq.producer;

import org.openbpm.sys.api.jms.MessageQueueSendException;
import org.openbpm.sys.api.jms.model.JmsDTO;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.openbpm.sys.simplemq.consumer.AbstractMessageQueue;

import java.util.List;

/**
 * 同步队列发送，内部应用
 *
 */
public class SynchronousQueueProducer extends AbstractMessageQueue implements JmsProducer {

    @Override
    public void sendToQueue(JmsDTO message) throws MessageQueueSendException {
        handleMessage(message);
    }

    @Override
    public void sendToQueue(List<JmsDTO> messages) throws MessageQueueSendException {
        if (messages != null && !messages.isEmpty()) {
            for (JmsDTO jmsDTO : messages) {
                sendToQueue(jmsDTO);
            }
        }
    }
}
