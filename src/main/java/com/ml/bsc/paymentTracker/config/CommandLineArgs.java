package com.ml.bsc.paymentTracker.config;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintStream;

public class CommandLineArgs {

    private Log log = LogFactory.getLog(CommandLineArgs.class);

    private Options options;

    private Option helpOption;
    private Option importFileOption;
    private Option currenciesConfigFileOption;

    private String importFile;
    private String currenciesConfigFile;

    public CommandLineArgs(String [] args) {
        options  = new Options();
        options.addOption(makeHelpOption());
        options.addOption(makeImportOption());
        options.addOption(makeCurrenciesConfigFileOption());

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption(helpOption.getOpt())) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "java -jar payment-tracker[-jar-with-dependencies].jar", options, true);
            }

            importFile = cmd.getOptionValue(importFileOption.getOpt());
            currenciesConfigFile = cmd.getOptionValue(currenciesConfigFileOption.getOpt(), "currencies.xml");

        } catch (ParseException e) {
            log.error("Error while loading config:", e);
        }
    }

    private Option makeHelpOption() {
        helpOption = Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("help documentation")
                .build();
        return helpOption;
    }

    private Option makeImportOption() {
        importFileOption = Option.builder("i")
                .longOpt("import")
                .required(false)
                .hasArg(true)
                .desc("file to import")
                .build();
        return importFileOption;
    }

    private Option makeCurrenciesConfigFileOption() {
        currenciesConfigFileOption = Option.builder("c")
                .longOpt("currencies-config")
                .required(false)
                .hasArg(true)
                .desc("file with currencies configuration")
                .build();
        return currenciesConfigFileOption;
    }

    public String getImportFile() {
        return importFile;
    }

    public String getCurrenciesConfigFile() {
        return currenciesConfigFile;
    }

}
