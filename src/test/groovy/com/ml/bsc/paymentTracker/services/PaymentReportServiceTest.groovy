package com.ml.bsc.paymentTracker.services

import com.ml.bsc.paymentTracker.config.CurrencyConfig
import com.ml.bsc.paymentTracker.storage.PaymentStorage
import spock.lang.Specification

class PaymentReportServiceTest extends Specification {

    PaymentReportService paymentReportService
    PaymentStorage paymentStorage
    CurrencyConfig currencyConfig

    ByteArrayOutputStream buffer

    def setup() {
        paymentStorage = Mock()
        paymentStorage.getPaymentsPerCurrency() >> [USD:1300L, HKD:10L, RMB:-500L]

        currencyConfig = new CurrencyConfig(["HKD": 0.13])

        buffer = new ByteArrayOutputStream()

        paymentReportService = new PaymentReportService(currencyConfig, paymentStorage, new PrintStream(buffer))
    }

    def "run scheduled reporting"() {
        given:
            def period = 200L
            def expectedOutput = """USD 1300
HKD 10 (USD 1.30)
RMB -500
""".normalize()

        when: "scheduling report with period #period"
            paymentReportService.startScheduledReporting(period)
        and: "waiting until first period completes"
            sleep(period + 50)
        then: "report appears in right format exactly once"
            buffer.toString().normalize() == expectedOutput

        when: "resetting output"
            buffer.reset()
        and: "waiting second period"
            sleep(period)
        and: "stopping scheduled task"
            paymentReportService.stopScheduledReporting()
        and: "waiting third period"
            sleep(period)
        then: "report appears in right format exactly once"
            buffer.toString().normalize() == expectedOutput

    }
}
