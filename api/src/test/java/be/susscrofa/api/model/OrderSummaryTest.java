package be.susscrofa.api.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderSummaryTest {

    @Test
    void getUnitPriceVatIncluded() {
        var orderSummary = OrderSummary
                .builder()
                .reduction(0)
                .vat(6)
                .quantity(2)
                .unitPrice(new BigDecimal("1.415"))
                .build();

        Assert.assertEquals(new BigDecimal("1.50"), orderSummary.getUnitPriceVatIncluded());
        Assert.assertEquals(new BigDecimal("3.00"), orderSummary.getTotalVatIncluded());
    }
}