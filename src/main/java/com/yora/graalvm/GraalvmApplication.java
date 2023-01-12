package com.yora.graalvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;

@SpringBootApplication
@AotProxyHint(targetClass = com.yora.graalvm.web.BulkUploadController.class, proxyFeatures = ProxyBits.IS_STATIC)
@AotProxyHint(targetClass = com.yora.graalvm.web.DataController.class, proxyFeatures = ProxyBits.IS_STATIC)
public class GraalvmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraalvmApplication.class, args);
	}

}
