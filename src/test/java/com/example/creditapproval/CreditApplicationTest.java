package com.example.creditapproval;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditApplicationTest {

    @Test
    void calculatesDebtRatioWithEightDecimalPlaces() {
        CreditApplication application = validApplication(new BigDecimal("600"), new BigDecimal("2500"));

        assertEquals(new BigDecimal("0.24000000"), application.debtRatio());
    }

    @Test
    void doesNotDivideWhenIncomeIsZero() {
        CreditApplication application = validApplication(new BigDecimal("600"), BigDecimal.ZERO);

        assertNull(application.debtRatio());
    }

    @Test
    void validatesTheStructuralInputContract() {
        assertTrue(validApplication(new BigDecimal("600"), new BigDecimal("2500")).hasValidInput());
        assertFalse(validApplication(new BigDecimal("600"), BigDecimal.ZERO).hasValidInput());
    }

    @Test
    void exposesTheBusinessStatesAsAnExplicitContract() {
        assertEquals("APPROVED", CreditApprovalStatus.APPROVED.name());
        assertEquals("MANUAL_REVIEW", CreditApprovalStatus.MANUAL_REVIEW.name());
        assertEquals("REJECTED", CreditApprovalStatus.REJECTED.name());
    }

    private static CreditApplication validApplication(BigDecimal debt, BigDecimal income) {
        return new CreditApplication(
                "CUST-001",
                720,
                income,
                debt,
                new BigDecimal("10000"),
                false);
    }
}
