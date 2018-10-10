package com.ml.bsc.paymentTracker.entities

import spock.lang.Specification
import spock.lang.Unroll

class PaymentTest extends Specification {

    def "parse payment from valid string"() {
        given:
            def text = "USD 1000"

        when:
            def payment = Payment.parseFrom(text)
        then:
            payment.currencyCode == "USD"
            payment.amount == 1000
    }

    @Unroll
    def "parse payment from invalid string: '#text'"() {
        when:
            Payment.parseFrom(text)
        then:
            thrown IllegalArgumentException

        where:
            text << [
                    null,
                    "",
                    "USD",
                    "USD D 1000",
                    "USD D",
                    "USD 100.5",
            ]
    }
}
