package com.ml.bsc.paymentTracker.services

import com.ml.bsc.paymentTracker.entities.Payment
import com.ml.bsc.paymentTracker.storage.PaymentStorage
import spock.lang.Specification
import spock.lang.Unroll;

class PaymentServiceTest extends Specification {

    PaymentService paymentService
    PaymentStorage paymentStorage

    def setup() {
        paymentStorage = Mock()
        paymentService = new PaymentService(paymentStorage)
    }

    def "save valid payment"() {
        given: "payment is valid"
            def payment = new Payment("USD", 1000)

        when: "saving valid payment"
            paymentService.save(payment)
        then: "it is send to storage"
            1 * paymentStorage.save({ it.size() == 1 && it[0] == payment })
    }

    @Unroll
    def "save invalid payment, currency code = '#currencyCode', amount = '#amount'"() {
        given: "payment is valid"
            def payment = new Payment(currencyCode, amount)

        when: "saving valid payment"
            paymentService.save(payment)
        then:
            thrown IllegalArgumentException

        where:
            currencyCode | amount
            null         | 1000
            "ab"         | 1000
            "abcd"       | 1000
            "USD"        | null
            "USD"        | 0
    }

    def "import payments from valid file"() {
        given:
            def fileContent = """USD 1000
USD 500
HKD 100
USD -200
RMD -500"""

        when: "importing file in valid format"
            paymentService.importFile(new ByteArrayInputStream(fileContent.bytes))
        then: "all payments are sent to storage"
            1 * paymentStorage.save({
                it.size() == 5 &&
                it[0].currencyCode == "USD" && it[0].amount == 1000 &&
                it[1].currencyCode == "USD" && it[1].amount == 500 &&
                it[2].currencyCode == "HKD" && it[2].amount == 100 &&
                it[3].currencyCode == "USD" && it[3].amount == -200 &&
                it[4].currencyCode == "RMD" && it[4].amount == -500
            })
    }

    @Unroll
    def "import payments from invalid file: '#fileContent'"() {
        when: "importing file in invalid format"
            paymentService.importFile(new ByteArrayInputStream(fileContent.bytes))
        then:
            thrown IllegalArgumentException

        where:
            fileContent << ["USD TEST 500", "USD TEST", "USD 500.5", "USD 9223372036854775808"]
    }
}