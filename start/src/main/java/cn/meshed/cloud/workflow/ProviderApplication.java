package cn.meshed.cloud.workflow;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h1>启动类</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@EnableDubbo(scanBasePackages = {"cn.meshed.cloud.workflow.*.remote"})
@MapperScan("cn.meshed.cloud.workflow.*.gatewayimpl.database.mapper")
@SpringBootApplication(scanBasePackages = {"cn.meshed.cloud", "com.alibaba.cola"})
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
