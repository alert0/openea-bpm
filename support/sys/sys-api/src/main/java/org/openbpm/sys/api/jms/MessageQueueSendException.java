package org.openbpm.sys.api.jms;

/**
 * 消息队列发送异常
 *
 *
 */
public class MessageQueueSendException extends RuntimeException {

    public MessageQueueSendException(String message) {
        super(message, null, false, false);
    }

}
