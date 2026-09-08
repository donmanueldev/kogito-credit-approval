package com.example.creditapproval;

import java.math.BigDecimal;

public final class CreditApplicationNormalizer {

    private CreditApplicationNormalizer() {
    }

    public static CreditApplication normalize(
            String customerId,
            Integer creditScore,
            BigDecimal monthlyIncome,
            BigDecimal monthlyDebt,
            BigDecimal requestedAmount,
            Boolean fraudConfirmed) {
        return normalize(new CreditApplication(
                customerId,
                creditScore,
                monthlyIncome,
                monthlyDebt,
                requestedAmount,
                fraudConfirmed));
    }

    public static CreditApplication normalize(CreditApplication application) {
        return new CreditApplication(
                valueOrDefault(application.customerId(), ""),
                valueOrDefault(application.creditScore(), -1),
                valueOrDefault(application.monthlyIncome(), BigDecimal.ZERO),
                valueOrDefault(application.monthlyDebt(), BigDecimal.ZERO),
                valueOrDefault(application.requestedAmount(), BigDecimal.ZERO),
                valueOrDefault(application.fraudConfirmed(), Boolean.FALSE));
    }

    private static <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
