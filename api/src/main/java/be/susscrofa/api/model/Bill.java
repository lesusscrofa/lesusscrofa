package be.susscrofa.api.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Bill {

    private Client client;

    private LocalDate createDate;

    private LocalDate from;

    private LocalDate to;

    private List<OrderSummary> ordersSummaries;

    public static Bill createBill(Client client, LocalDate from, LocalDate to, BiFunction<LocalDate, LocalDate, List<OrderSummary>> orderSummaryFactory) {
        var ordersSummaries = orderSummaryFactory.apply(from, to)
                .stream()
                .map(os -> os.reduction(client.getReduction() != null ? client.getReduction() : 0))
                .collect(toList());

        return Bill
                .builder()
                .client(client)
                .createDate(LocalDate.now())
                .from(from)
                .to(to)
                .ordersSummaries(ordersSummaries)
                .build();
    }

    public BigDecimal getTotalVatExcluded() {
        return ordersSummaries
                .stream()
                .map(OrderSummary::getTotalVatExcluded)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalVat() {
        return ordersSummaries
                .stream()
                .map(OrderSummary::getTotalVat)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalVatIncluded() {
        return ordersSummaries
                .stream()
                .map(OrderSummary::getTotalVatIncluded)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
