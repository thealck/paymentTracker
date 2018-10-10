package com.ml.bsc.paymentTracker.storage;

import com.ml.bsc.paymentTracker.entities.Payment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * Payment memory storage
 */
public class PaymentStorageInMemory implements PaymentStorage {

    /**
     * For caching of aggregated payments.
     * Number of shards is set to 1 and can be increased in case high concurrency is expected.
     * Initial size is set to 10 and it can be changed depending on expected amount of processed currencies.
     */
    private ConcurrentHashMap<String, LongAdder> paymentsPerCurrency = new ConcurrentHashMap<>(10, 0.75f, 1);

    /**
     * Used for it's O(1) complexity when adding elements.
     */
    private ConcurrentLinkedQueue<Payment> payments = new ConcurrentLinkedQueue<>();

    /**
     * Saves payment into memory and recalculates payments per currency in cache.
     * Possible issue: block is not synchronized so paymentsPerCurrency might not contain latest payments when it's retrieved.
     * Solution is to lock read until save is completed but that would have performance cost.     *
     * @param payment to save
     */
    public void save(Payment payment) {
        payments.offer(payment);
        paymentsPerCurrency.computeIfAbsent(payment.getCurrencyCode(), x -> new LongAdder())
                .add(payment.getAmount());
    }


    public void save(List<Payment> payments) {
        for (Payment aPayment : payments) {
            save(aPayment);
        }
    }

    public Map<String, Long> getPaymentsPerCurrency() {
        return paymentsPerCurrency.entrySet().stream()
                .filter(x -> x.getValue().sum() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().sum()));
    }
}
