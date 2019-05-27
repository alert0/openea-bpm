package org.openbpm.bpm.engine.action.helper;

import com.alibaba.fastjson.JSON;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.engine.action.cmd.FlowRequestParam;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.util.ContextUtil;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class NewThreadActionUtil {
    protected static Logger LOG = LoggerFactory.getLogger(NewThreadActionUtil.class);

    public static void newThreadDoAction(InstanceActionCmd newFlowCmd, IUser startUser) {
        StartNewFlow triggerNewFlow = (StartNewFlow) AppUtil.getBean(StartNewFlow.class);
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            triggerNewFlow.setLatch(latch);
            triggerNewFlow.setUser(startUser);
            triggerNewFlow.setInstanceCmd(newFlowCmd);
            triggerNewFlow.setTransactionResource(TransactionSynchronizationManager.getResourceMap());
            executor.execute(triggerNewFlow);
            latch.await();
            if (triggerNewFlow.getException() != null) {
                throw new RuntimeException(triggerNewFlow.getException());
            }
            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("触发新流程失败 ！ ：" + e.getMessage());
        } catch (Throwable th) {
            executor.shutdown();
            throw th;
        }
    }

    public static void testStartScript() {
        newThreadDoAction(new DefaultInstanceActionCmd(new FlowRequestParam("404120998984024065", "start", JSON.parseObject("{\"Demo\":{\"bmId\":\"20000000280001\",\"bm\":\"科技部\",\"zd1\":\"JS初始化\",\"DemoSubList\":[{\"ms\":\"请开启控制台，或者查看表单源码，来查看我是如何初始化的\"}],\"mz\":\"测试新线程处理任务！\"}}"))), ContextUtil.getCurrentUser());
        LOG.debug("启动成功！");
    }
}
