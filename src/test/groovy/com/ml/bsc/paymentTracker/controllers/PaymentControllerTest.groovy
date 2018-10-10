package com.ml.bsc.paymentTracker.controllers

import com.ml.bsc.paymentTracker.services.PaymentService
import spock.lang.Specification

class PaymentControllerTest extends Specification {

    PaymentService paymentService
    PaymentController paymentController

    def setup() {
        paymentService = Mock()
        paymentController = new PaymentController(paymentService)
    }

    def "read payments from input stream"() {
        given:
            def input = """USD 1000
USD INVALID
HKD 100
USD -200
RMD -500
quit
"""

        when:
            paymentController.readInput(new ByteArrayInputStream(input.bytes))
        then:
            with(paymentService) {
                1 * save({ it.currencyCode == "USD" && it.amount == 1000})
                1 * save({ it.currencyCode == "HKD" && it.amount == 100})
                1 * save({ it.currencyCode == "USD" && it.amount == -200})
                1 * save({ it.currencyCode == "RMD" && it.amount == -500})
            }
    }
}
