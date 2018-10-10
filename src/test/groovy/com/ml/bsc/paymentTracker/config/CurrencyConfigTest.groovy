package com.ml.bsc.paymentTracker.config

import spock.lang.Specification

class CurrencyConfigTest extends Specification {

    def "load config from 'currency.xml' file"() {
        when: "creating configuration from file"
            def config = CurrencyConfig.loadConfigFromFile("currencies.xml")
        then: "config class is instantiated"
            config != null
        and: "it contains conversion rates from file"
            config.getConversionRate("USD") == null
            config.getConversionRate("HKD") == BigDecimal.valueOf(0.13)
            config.getConversionRate("RMB") == BigDecimal.valueOf(2)
    }
}
