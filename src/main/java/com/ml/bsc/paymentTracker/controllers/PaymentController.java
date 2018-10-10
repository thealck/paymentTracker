package com.ml.bsc.paymentTracker.controllers;

import com.ml.bsc.paymentTracker.entities.Payment;
import com.ml.bsc.paymentTracker.services.PaymentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Controller class that reads payments from input stream
 */
public class PaymentController {

    private Log log = LogFactory.getLog(PaymentController.class);

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Imports data from stream
     * @param inputStream input data
     */
    public void importFile(InputStream inputStream) {
        try {
            paymentService.importFile(inputStream);
        }
        catch (IllegalArgumentException | IOException e) {
            log.error("Error while processing input stream:", e);
        }
    }

    /**
     * Blocking method that reads input stream as Payments (that are stored) until 'quit' arrives
     * @param inputStream input data
     */
    public void readInput(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        String inputLine = in.nextLine();

        while (!"quit".equals(inputLine)) {
            try {
                Payment payment = Payment.parseFrom(inputLine);
                paymentService.save(payment);
            }
            catch (IllegalArgumentException e) {
                log.error("Error while processing input stream:", e);
            }
            inputLine = in.nextLine();
        }
    }

}
