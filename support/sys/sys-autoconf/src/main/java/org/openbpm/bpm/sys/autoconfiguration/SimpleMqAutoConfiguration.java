package org.openbpm.bpm.sys.autoconfiguration;

import cn.hutool.extra.mail.MailAccount;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.openbpm.sys.simplemq.producer.SynchronousQueueProducer;
import org.openbpm.sys.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 缓存相关配置
 *
 */
@Configuration
@EnableConfigurationProperties({MQMailConfigProperties.class, AbSimpleMessageQueueProperties.class})
public class SimpleMqAutoConfiguration {

    @Autowired
    private MQMailConfigProperties mQMailConfigProperties;

    /**
     * 默认消息发送提供者
     *
     * @return 消息发送提供端
     */
    @ConditionalOnExpression("'${ab.simple-mq.message-queue-type:synchronous}'.toLowerCase() == 'synchronous'")
    @Bean
    public JmsProducer synchronousQueueProducer() {
        return new SynchronousQueueProducer();
    }

    @Component
    class MailAccountAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            if (event.getApplicationContext().getParent() == null) {
                setEmailConfiguration();
            }
        }
    }

    private void setEmailConfiguration() {
        MailAccount account = new MailAccount();

        account.setHost(mQMailConfigProperties.getSendHost());
        account.setPort(mQMailConfigProperties.getSendPort());
        account.setFrom(mQMailConfigProperties.getMailAddress());
        account.setUser(mQMailConfigProperties.getNickName());
        account.setPass(mQMailConfigProperties.getPassword());
        account.setSslEnable(mQMailConfigProperties.isSSL());

        EmailUtil.setAccount(account);
    }

}