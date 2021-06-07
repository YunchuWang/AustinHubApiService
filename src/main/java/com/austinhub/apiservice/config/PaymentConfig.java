package com.austinhub.apiservice.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX,
                "w6q3k6s6ky2m4r5g",
                "q76r62tq97dyzjps",
                "c540ffd059a7e09643215162860128a3"
        );
    }
}
