package org.openbpm.base.core.executor.checker;

import org.springframework.stereotype.Service;

/**
 * 不可用 的 校验都不通过
 *
 */
@Service
public class DisabledExecutorChecker extends AbstractExecutorChecker {

    @Override
    public String getName() {
        return "不可用的校验器";
    }

    @Override
    public boolean check(String pluginKey) {
        return false;
    }

}
