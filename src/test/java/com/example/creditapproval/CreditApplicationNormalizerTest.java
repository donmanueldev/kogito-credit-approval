package com.example.creditapproval;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreditApplicationNormalizerTest {

    @Test
    void replacesMissingValuesWithSafeSentinelsForTheBpmnContext() {
        CreditApplication normalized = CreditApplicationNormalizer.normalize(
                null, null, null, null, null, null);

        assertEquals("", normalized.customerId());
        assertEquals(-1, normalized.creditScore());
        assertEquals(BigDecimal.ZERO, normalized.monthlyIncome());
        assertEquals(BigDecimal.ZERO, normalized.monthlyDebt());
        assertEquals(BigDecimal.ZERO, normalized.requestedAmount());
        assertFalse(normalized.fraudConfirmed());
        assertFalse(normalized.hasValidInput());
    }
}
