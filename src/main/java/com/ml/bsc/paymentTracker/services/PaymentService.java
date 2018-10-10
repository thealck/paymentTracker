package com.ml.bsc.paymentTracker.services;

import com.ml.bsc.paymentTracker.entities.Payment;
import com.ml.bsc.paymentTracker.storage.PaymentStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Payment service
 */
public class PaymentService {

    private PaymentStorage paymentStorage;

    public PaymentService(PaymentStorage paymentStorage) {
        this.paymentStorage = paymentStorage;
    }

    /**
     * Saves payment to storage
     * @param payment to save
     */
    public void save(Payment payment) {
        save(Arrays.asList(payment));
    }

    /**
     * Saves payments to storage
     * @param payments to save
     */
    public void save(List<Payment> payments) {
        for (Payment aPayment : payments) {
            validatePayment(aPayment);
        }
        paymentStorage.save(payments);
    }

    /**
     * Saves payments in provided file to storage
     * @param inputStream stream with file content
     * @throws IOException error while reading input stream
     */
    public void importFile(InputStream inputStream) throws IOException {
        List<Payment> payments = parseFile(inputStream);
        save(payments);
    }

    private List<Payment> parseFile(InputStream inputStream) throws IOException {
        List<Payment> payments = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            Payment payment = Payment.parseFrom(line);
            payments.add(payment);
        }
        reader.close();
        return payments;
    }

    private void validatePayment(Payment payment) {
        if (payment.getCurrencyCode() == null || payment.getCurrencyCode().length() != 3) {
            throw new IllegalArgumentException("Payment currency code must be 3 characters long (current value is '" + payment.getCurrencyCode() + "')");
        }
        if (payment.getAmount() == null || payment.getAmount() == 0) {
            throw new IllegalArgumentException("Payment amount cannot be 0"); // I assume that transaction for 0 wouldn't make sense
        }
    }

}
