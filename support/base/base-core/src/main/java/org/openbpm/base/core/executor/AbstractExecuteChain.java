package org.openbpm.base.core.executor;

import org.openbpm.base.api.executor.ExecuteChain;

/**
 * 抽象的执行链
 * 这里只是作为一种子类会有多个实现的标记
 * 具体看例子吧-。-
 *
 * @param <T>
 */
public abstract class AbstractExecuteChain<T> extends AbstractExecutor<T> implements ExecuteChain<T> {

}
