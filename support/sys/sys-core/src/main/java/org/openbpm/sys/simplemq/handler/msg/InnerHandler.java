package org.openbpm.sys.simplemq.handler.msg;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import org.openbpm.sys.api.jms.model.msg.NotifyMessage;

/**
 * 内部消息处理器
 *
 */
@Component("innerHandler")
public class InnerHandler extends AbsNotifyMessageHandler<NotifyMessage> {

    @Override
    public String getType() {
        return "inner";
    }


    public String getTitle() {
        return "内部消息";
    }


    public boolean getIsDefault() {
        return false;
    }


    public boolean getSupportHtml() {
        return true;
    }

	public boolean handlerMessage() {
		return false;
	}

	@Override
	public boolean sendMessage(NotifyMessage message) {
        LOGGER.warn("站内消息发送尚未实现："+ JSON.toJSONString(message));
        return false;
	}

}
