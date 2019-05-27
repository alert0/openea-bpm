package org.openbpm.bpm.base.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import org.openbpm.base.core.id.IdGenerator;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.id.snowflake.SnowflakeIdGenerator;
import org.openbpm.base.core.id.snowflake.SnowflakeIdMeta;

/**
 * id生成器bean配置
 *
 *
 * @date 2018-07-10
 */
@Configuration
@EnableConfigurationProperties(IdGeneratorProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class IdGeneratorAutoConfiguration {

    @Bean
    public IdGenerator defaultIdGenerator(JdbcTemplate jdbcTemplate, IdGeneratorProperties idGeneratorProperties){
    	SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(new SnowflakeIdMeta(idGeneratorProperties.getMachine(), idGeneratorProperties.getMachineBits(), idGeneratorProperties.getSequenceBits(), idGeneratorProperties.getTimeSequence()));
        return idGenerator;
    }

    @Bean
    public IdUtil uniqueIdUtil(IdGenerator idGenerator){
        IdUtil uniqueIdUtil = new IdUtil();
        uniqueIdUtil.setIdGenerator(idGenerator);
        return uniqueIdUtil;
    }
}
