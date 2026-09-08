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

    private static final int DEBT_RATIO_SCALE = 8;

    public BigDecimal debtRatio() {
        if (!hasPositiveIncome() || monthlyDebt == null) {
            return null;
        }

        return monthlyDebt.divide(monthlyIncome, DEBT_RATIO_SCALE, RoundingMode.HALF_UP);
    }

    public boolean hasValidInput() {
        return hasCustomerId()
                && hasNonNegativeCreditScore()
                && hasPositiveIncome()
                && hasNonNegativeDebt()
                && hasPositiveRequestedAmount()
                && fraudConfirmed != null;
    }
    private boolean hasCustomerId() {
        return customerId != null && !customerId.isBlank();
    }

    private boolean hasNonNegativeCreditScore() {
        return creditScore != null && creditScore >= 0;
    }

    private boolean hasPositiveIncome() {
        return monthlyIncome != null && monthlyIncome.signum() > 0;
    }

    private boolean hasNonNegativeDebt() {
        return monthlyDebt != null && monthlyDebt.signum() >= 0;
    }

    private boolean hasPositiveRequestedAmount() {
        return requestedAmount != null && requestedAmount.signum() > 0;
    }
}
