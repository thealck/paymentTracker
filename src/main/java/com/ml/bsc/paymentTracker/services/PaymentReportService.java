package com.ml.bsc.paymentTracker.services;

import com.ml.bsc.paymentTracker.config.CurrencyConfig;
import com.ml.bsc.paymentTracker.storage.PaymentStorage;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Reporting service for payments which writes aggregated data per currency to output stream
 */
public class PaymentReportService {

    private CurrencyConfig currencyConfig;
    private PaymentStorage paymentStorage;
    private PrintStream outputStream;

    private ScheduledExecutorService executor;

    public PaymentReportService(CurrencyConfig currencyConfig, PaymentStorage paymentStorage, PrintStream outputStream) {
        this.currencyConfig = currencyConfig;
        this.paymentStorage = paymentStorage;
        this.outputStream = outputStream;
    }

    /**
     * Starts periodic task that reports payment statistics. First report is scheduled after
     * @param period of scheduled task in ms
     */
    public void startScheduledReporting(long period) {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new PaymentReportTask(), period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops periodic task that reports payment statistics
     */
    public void stopScheduledReporting() {
        executor.shutdown();
    }

    /**
     * Periodic task that retrieved aggregated data, conversion rates and outputs them to console
     */
    class PaymentReportTask extends TimerTask {

        @Override
        public void run() {
            for (Map.Entry<String, Long> currencyReport : paymentStorage.getPaymentsPerCurrency().entrySet()) {
                String currencyCode = currencyReport.getKey();
                Long totalAmount = currencyReport.getValue();
                BigDecimal conversionRate = currencyConfig.getConversionRate(currencyCode);

                String report = currencyCode + " " + totalAmount;
                if (conversionRate != null) {
                    BigDecimal totalAmountUSD = conversionRate.multiply(BigDecimal.valueOf(totalAmount));
                    report += " (USD " + totalAmountUSD + ")";
                }
                outputStream.println(report);
            }
        }

    }


}
