package be.susscrofa.api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BillTest {

    @Test
    public void computeBillWithoutReduction() {
        var client = Client
                .builder()
                .reduction(0)
                .build();

        var orderSummaries = List.of(OrderSummary
                .builder()
                .unitPrice(BigDecimal.TEN)
                .vat(21)
                .quantity(1)
                .build(),
            OrderSummary
                .builder()
                .unitPrice(BigDecimal.TEN)
                .vat(21)
                .quantity(1)
                .build());

        var bill = Bill.createBill(client, LocalDate.now(), LocalDate.now(), (d1, d2) -> orderSummaries);

        assertThat(bill.getTotalVatExcluded()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(bill.getTotalVatIncluded()).isEqualTo(new BigDecimal("24.20"));
        assertThat(bill.getTotalVat()).isEqualTo(new BigDecimal("4.20"));
    }

    @Test
    public void computeBillWithReduction() {
        var client = Client
                .builder()
                .reduction(10)
                .build();

        var orderSummaries = List.of(OrderSummary
                .builder()
                .unitPrice(BigDecimal.TEN)
                .quantity(1)
                .vat(21)
                .build(),
            OrderSummary
                .builder()
                .unitPrice(BigDecimal.valueOf(9.5))
                .quantity(1)
                .vat(21)
                .build(),
            OrderSummary
                .builder()
                .unitPrice(BigDecimal.TEN)
                .quantity(1)
                .vat(21)
                .build());

        var bill = Bill.createBill(client, LocalDate.now(), LocalDate.now(), (d1, d2) -> orderSummaries);

        assertThat(bill.getTotalVatExcluded()).isEqualTo(BigDecimal.valueOf(26.55));
        assertThat(bill.getTotalVatIncluded()).isEqualTo(new BigDecimal("32.1255"));
        assertThat(bill.getTotalVat()).isEqualTo(new BigDecimal("5.5755"));
    }
}
