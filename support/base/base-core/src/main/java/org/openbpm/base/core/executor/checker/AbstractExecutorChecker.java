package org.openbpm.base.core.executor.checker;

import org.openbpm.base.api.executor.checker.ExecutorChecker;

/**
 * 执行器的校验者的抽象类
 *
 */
public abstract class AbstractExecutorChecker implements ExecutorChecker {
    /**
     * <pre>
     * 校验器的key
     * 默认是类名
     * </pre>
     *
     * @return
     */
    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }
}
