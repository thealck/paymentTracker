package com.ml.bsc.paymentTracker;

import com.ml.bsc.paymentTracker.config.CommandLineArgs;
import com.ml.bsc.paymentTracker.config.CurrencyConfig;
import com.ml.bsc.paymentTracker.controllers.PaymentController;
import com.ml.bsc.paymentTracker.services.PaymentReportService;
import com.ml.bsc.paymentTracker.services.PaymentService;
import com.ml.bsc.paymentTracker.storage.PaymentStorage;
import com.ml.bsc.paymentTracker.storage.PaymentStorageInMemory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;

public class Application {

    private static Log log = LogFactory.getLog(Application.class);

    public static void main(String [] args) {
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);
        CurrencyConfig currencyConfig = CurrencyConfig.loadConfigFromFile(commandLineArgs.getCurrenciesConfigFile());

        PaymentStorage paymentStorage = new PaymentStorageInMemory();
        PaymentService paymentService = new PaymentService(paymentStorage);
        PaymentReportService paymentReportService = new PaymentReportService(currencyConfig, paymentStorage, System.out);
        PaymentController paymentController = new PaymentController(paymentService);

        try {
            paymentReportService.startScheduledReporting(60000L);

            if(commandLineArgs.getImportFile() != null) {
                paymentController.importFile(new FileInputStream(commandLineArgs.getImportFile()));
            }

            paymentController.readInput(System.in);

        } catch (Exception e) {
            log.fatal(e);
        } finally {
            paymentReportService.stopScheduledReporting();
        }
    }
}
