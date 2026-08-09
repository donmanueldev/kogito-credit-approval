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
        return new CreditApplication(
                customerId == null ? "" : customerId,
                creditScore == null ? -1 : creditScore,
                monthlyIncome == null ? BigDecimal.ZERO : monthlyIncome,
                monthlyDebt == null ? BigDecimal.ZERO : monthlyDebt,
                requestedAmount == null ? BigDecimal.ZERO : requestedAmount,
                fraudConfirmed == null ? Boolean.FALSE : fraudConfirmed);
    }
}
