package com.rop.orderprocessingapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = 'api')
class ApiProperties {
    static final int perPageDefault = 10
    static final int perPageMax = 300
    static final int pageNumberDefault = 0
}
