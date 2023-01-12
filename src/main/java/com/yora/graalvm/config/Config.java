package com.yora.graalvm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan(basePackages  = "com.yora.graalvm.entity")
@EnableJpaRepositories(basePackages = "com.yora.graalvm.dao")
@Configuration
public class Config {

}
