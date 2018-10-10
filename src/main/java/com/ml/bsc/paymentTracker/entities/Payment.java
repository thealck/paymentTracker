package com.ml.bsc.paymentTracker.entities;

public class Payment {

    private String currencyCode;

    /**
     * Example shows only integers, in case real numbers should be supported I would use BigDecimal if amount would
     * allow for values with more then 15 significant digits or double otherwise
     */
    private Long amount;

    public Payment(String currencyCode, Long amount) {
        if (currencyCode != null) {
            this.currencyCode = currencyCode.toUpperCase();
        }
        this.amount = amount;
    }

    public static Payment parseFrom(String text) {
        if(text == null) {
            throw new IllegalArgumentException("String is empty.");
        }

        String[] values = text.trim().split(" ");

        if (values.length != 2) {
            throw new IllegalArgumentException("String is in invalid format (value: '" + text + "').");
        }
        String currencyCode = values[0];

        Long amount;
        try {
            amount = Long.parseLong(values[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be long (value: '" + values[1] + "')");
        }

        return new Payment(currencyCode, amount);
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

}
