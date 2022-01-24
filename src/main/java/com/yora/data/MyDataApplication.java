package com.yora.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.nativex.hint.*;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.yora.data.dao")
@AotProxyHint(targetClass = com.yora.data.rest.BulkUploadController.class, proxyFeatures = ProxyBits.IS_STATIC)
@AotProxyHint(targetClass = com.yora.data.rest.DataController.class, proxyFeatures = ProxyBits.IS_STATIC)
@JdkProxyHint(
        types = {
                org.springframework.data.jpa.repository.support.CrudMethodMetadata.class,
               org.springframework.aop.SpringProxy.class,
                org.springframework.aop.framework.Advised.class,
                org.springframework.core.DecoratingProxy.class
        })
public class MyDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDataApplication.class, args);
    }

}
