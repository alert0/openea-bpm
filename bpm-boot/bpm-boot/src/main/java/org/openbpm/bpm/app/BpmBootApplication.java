package org.openbpm.bpm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 *
 */
@ComponentScan("org.openbpm.*")
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class BpmBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmBootApplication.class, args);
    }
    
    

}
