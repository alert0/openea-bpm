package org.openbpm.sys.simplemq.handler.msg;

import com.alibaba.fastjson.JSON;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.jms.model.msg.NotifyMessage;
import org.openbpm.sys.api.service.SysIdentityConvert;
import org.openbpm.sys.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 邮件消息处理器。
 *
 */
@Component("mailHandler")
public class MailHandler extends AbsNotifyMessageHandler<NotifyMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailHandler.class);

    @Resource
    SysIdentityConvert sysIdentityConvert;

    @Override
    public String getType() {
        return "email";
    }


    @Override
    public String getTitle() {
        return "邮件";
    }
    
    @Override
    public boolean getIsDefault() {
    	return true;
    }
  

    @Override
    public boolean getSupportHtml() {
        return true;
    }

    @Override
    public boolean sendMessage(NotifyMessage notifMessage) {
        List<? extends IUser> recievers = sysIdentityConvert.convert2Users(notifMessage.getReceivers());
        for (IUser reciver : recievers) {
            String email = reciver.getEmail();
            if (StringUtil.isEmpty(email)) {
                continue;
            }
            try {
                EmailUtil.send(email, notifMessage.getSubject(), notifMessage.getHtmlContent());
            } catch (Exception e) {
                LOGGER.error("发送邮件失败!, 发送参数：{}", JSON.toJSONString(notifMessage), e);
            }
        }
        LOGGER.debug("发送邮件成功 ：{}", JSON.toJSONString(notifMessage));
        return true;
    }

}
