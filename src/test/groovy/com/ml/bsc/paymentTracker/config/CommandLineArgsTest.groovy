package com.ml.bsc.paymentTracker.config

import spock.lang.Specification
import spock.lang.Unroll

class CommandLineArgsTest extends Specification {

    @Unroll
    def "parse command line arguments, args: '#args'"() {
        when:
            def cmdArgs = new CommandLineArgs(args as String[])
        then:
            cmdArgs.importFile == importFile
            cmdArgs.currenciesConfigFile == currenciesConfigFile

        where:
            args                                   | importFile                    | currenciesConfigFile
            [""]                                   | null                          | "currencies.xml"
            ["-i","test.txt"]                      | "test.txt"                    | "currencies.xml"
            ["--import=test.txt"]                  | "test.txt"                    | "currencies.xml"
            ["-i","/home/user/test.txt"]           | "/home/user/test.txt"         | "currencies.xml"
            ["-i","C:\\Program Files\\test.txt"]   | "C:\\Program Files\\test.txt" | "currencies.xml"
            ["-c","test.xml"]                      | null                          | "test.xml"
            ["-i","test.txt","-c","test.xml"]      | "test.txt"                    | "test.xml"
            ["-c","test.xml","-i","test.txt"]      | "test.txt"                    | "test.xml"

    }
}
