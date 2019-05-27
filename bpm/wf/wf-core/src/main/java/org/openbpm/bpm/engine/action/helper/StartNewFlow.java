package org.openbpm.bpm.engine.action.helper;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.util.ContextUtil;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Scope("prototype")
@Component
public class StartNewFlow implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(StartNewFlow.class);
    private static String startingNewFlow = "startingNewFlow";
    private Exception exception;
    InstanceActionCmd instanceCmd;
    private CountDownLatch latch;
    private Map<Object, Object> transactionResource;
    IUser user;

    public void run() {
        setCurrentTransactionResource();
        if (this.instanceCmd == null || this.user == null) {
            throw new BusinessException("启动新流程失败！ new flow Cmd or starUser cannot be null");
        }
        try {
            ContextUtil.setCurrentUser(this.user);
            this.instanceCmd.executeCmd();
            logger.debug("新流程启动成功！ ");
            this.latch.countDown();
        } catch (Exception e) {
            this.exception = e;
            this.latch.countDown();
            logger.error("启动新流程流程失败！" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isInStartNewFlow() {
        return false;
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }

    public void setLatch(CountDownLatch latch2) {
        this.latch = latch2;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception2) {
        this.exception = exception2;
    }

    public void setUser(IUser user2) {
        this.user = user2;
    }

    public InstanceActionCmd getInstanceCmd() {
        return this.instanceCmd;
    }

    public void setInstanceCmd(InstanceActionCmd instanceCmd2) {
        this.instanceCmd = instanceCmd2;
    }

    private void setCurrentTransactionResource() {
        if (!MapUtils.isEmpty(this.transactionResource)) {
            for (Entry<Object, Object> entry : this.transactionResource.entrySet()) {
                TransactionSynchronizationManager.bindResource(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setTransactionResource(Map<Object, Object> transactionResource2) {
        this.transactionResource = transactionResource2;
    }
}
