package com.example.creditapproval;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CreditApplication(
        String customerId,
        Integer creditScore,
        BigDecimal monthlyIncome,
        BigDecimal monthlyDebt,
        BigDecimal requestedAmount,
        Boolean fraudConfirmed) {

    public BigDecimal debtRatio() {
        if (monthlyIncome == null || monthlyIncome.signum() <= 0 || monthlyDebt == null) {
            return null;
        }

        return monthlyDebt.divide(monthlyIncome, 8, RoundingMode.HALF_UP);
    }

    public boolean hasValidInput() {
        return customerId != null
                && !customerId.isBlank()
                && creditScore != null
                && creditScore >= 0
                && monthlyIncome != null
                && monthlyIncome.signum() > 0
                && monthlyDebt != null
                && monthlyDebt.signum() >= 0
                && requestedAmount != null
                && requestedAmount.signum() > 0
                && fraudConfirmed != null;
    }
}
