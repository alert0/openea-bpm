package org.openbpm.base.core.executor.checker;

import org.springframework.stereotype.Service;

/**
 * 免费的校验器 校验都通过
 *
 */
@Service
public class FreeExecutorChecker extends AbstractExecutorChecker {

    @Override
    public String getName() {
        return "免费的校验器";
    }

    @Override
    public boolean check(String pluginKey) {
        return true;
    }

}
