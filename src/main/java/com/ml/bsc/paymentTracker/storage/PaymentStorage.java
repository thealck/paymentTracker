package com.ml.bsc.paymentTracker.storage;

import com.ml.bsc.paymentTracker.entities.Payment;

import java.util.List;
import java.util.Map;

/**
 * Payment storage
 */
public interface PaymentStorage {
    /**
     * @param payment to save
     */
    void save(Payment payment);

    /**
     * @param payments to save
     */
    void save(List<Payment> payments);

    /**
     * @return map with payments aggregated by currency (without currencies with sum 0)
     */
    Map<String, Long> getPaymentsPerCurrency();
}
