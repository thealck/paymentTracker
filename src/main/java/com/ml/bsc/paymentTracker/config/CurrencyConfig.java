package com.ml.bsc.paymentTracker.config;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class CurrencyConfig {

    private static Log log = LogFactory.getLog(CurrencyConfig.class);

    private static HashMap<String, CurrencyConfig> instances = new HashMap<>();

    private HashMap<String, BigDecimal> conversionRates;

    public CurrencyConfig(HashMap<String, BigDecimal> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public static CurrencyConfig loadConfigFromFile(String filePath) {
        if (!instances.containsKey(filePath)) {
            instances.put(filePath, new CurrencyConfig(loadConversionRatesFromFile(filePath)));
        }
        return instances.get(filePath);
    }

    private static HashMap<String, BigDecimal> loadConversionRatesFromFile(String filePath) {
        HashMap<String, BigDecimal> res = new HashMap<>();

        Configurations configs = new Configurations();
        try {
            XMLConfiguration config = configs.xml(filePath);
            List<HierarchicalConfiguration<ImmutableNode>> fields = config.configurationsAt("currency");

            for (HierarchicalConfiguration sub : fields) {
                String code = sub.getString("code");
                BigDecimal conversionRateToUSD = new BigDecimal(sub.getString("conversionRateToUSD"));
                res.put(code, conversionRateToUSD);
            }
        } catch (ConfigurationException e) {
            log.error("Error while loading conversion rates:", e);
        }

        return res;
    }

    public BigDecimal getConversionRate(String currencyCode) {
        return conversionRates.get(currencyCode);
    }

}
