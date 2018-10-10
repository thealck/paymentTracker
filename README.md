# Payment Tracker

## Build

To build app just run `mvn package` and it will output `.jar` file to target subdirectory.

## Configuration

### Currencies conversion rates

Conversion rates are defined in `.xml` file in following format:
```$xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<currencies>
    <currency>
        <code>HKD</code>
        <conversionRateToUSD>0.13</conversionRateToUSD>
    </currency>
    <currency>
        <code>RMB</code>
        <conversionRateToUSD>2</conversionRateToUSD>
    </currency>
</currencies>
```
Default location for this file is `currencies.xml` but it can be changed using `-c,--currencies-config <arg>` argument.

## Run 

There is help available for command line arguments:
```
usage: java -jar payment-tracker[-jar-with-dependencies].jar [-c <arg>] [-h] [-i <arg>]
 -c,--currencies-config <arg>   file with currencies configuration
 -h,--help                      help documentation
 -i,--import <arg>              file to import
```
Depending on your local libraries you might want to use `payment-tracker-jar-with-dependencies.jar`.
