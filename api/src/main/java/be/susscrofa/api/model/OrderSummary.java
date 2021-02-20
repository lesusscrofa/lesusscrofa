package be.susscrofa.api.model;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummary {

    private long id;

    private String name;

    private Formula formula;

    private BigDecimal unitPrice;

    private int vat;

    private int quantity;

    private int reduction;

    private String unit;

    public BigDecimal getUnitPriceReductionIncluded() {
        return unitPrice.multiply(BigDecimal.valueOf(100 - reduction).divide(BigDecimal.valueOf(100)));
    }

    public BigDecimal getUnitPriceVatIncluded() {
        return getUnitPriceReductionIncluded()
                .multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal getTotalVatExcluded() {
        return getUnitPriceReductionIncluded()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalVat() {
        return getTotalVatExcluded()
                .multiply(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100)));
    }

    public BigDecimal getTotalVatIncluded() {
        return getTotalVatExcluded()
                .multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
    }

    public String getDescription() {
        StringBuilder description = new StringBuilder();

        description.append(formula == Formula.OTHER ? name : formula.getName());
        description.append(" à ");
        description.append(getUnitPriceReductionIncluded() + " €");

        return description.toString();
    }

    public OrderSummary reduction(int reduction) {
        this.reduction = reduction;

        return this;
    }
}
