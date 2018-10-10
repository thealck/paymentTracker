package com.ml.bsc.paymentTracker.storage

import com.ml.bsc.paymentTracker.entities.Payment
import spock.lang.Specification

class PaymentStorageInMemoryTest extends Specification {

    void setupAll() {
        PaymentStorageInMemory.class.getDeclaredField("payments").setAccessible(true)
    }

    PaymentStorageInMemory paymentStorage = new PaymentStorageInMemory()

    def "save payment to storage"() {
        given: "payment is valid"
            def payment = new Payment("USD", 1000)

        when: "saving payment"
            paymentStorage.save(payment)
        then: "it is stored in collection"
            paymentStorage.payments.size() == 1
            paymentStorage.payments.peek() == payment
        and: "it is added to aggregated data"
            paymentStorage.getPaymentsPerCurrency().size() == 1
            paymentStorage.getPaymentsPerCurrency()[payment.currencyCode] == payment.amount
    }

    def "save multiple payments to storage"() {
        given: "all payments are valid"
            def payments = [
                    new Payment("USD", 1000),
                    new Payment("USD", 500),
                    new Payment("HKD", 100),
                    new Payment("USD", -200),
                    new Payment("RMB", -500),
            ] as List<Payment>

        when:
            paymentStorage.save(payments)
        then: "all payments are stored in collection"
            paymentStorage.payments.size() == 5
        and: "it is added to aggregated data"
            paymentStorage.getPaymentsPerCurrency().size() == 3
            paymentStorage.getPaymentsPerCurrency()["USD"] == 1300
            paymentStorage.getPaymentsPerCurrency()["HKD"] == 100
            paymentStorage.getPaymentsPerCurrency()["RMB"] == -500
    }
}