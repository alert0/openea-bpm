package org.openbpm.form.generator;

import org.springframework.stereotype.Component;

/**
 * //TODO 需要修改满足VUE模板
 */

@Component
public class VueFormElementGenerator extends PcFormElementGenerator {

    @Override
    public String getGeneratorName() {
        return "vueGenerator";
    }
}