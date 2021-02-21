package be.susscrofa.api.model;


import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    private BigDecimal computeUnitPriceReductionIncluded() {
        return unitPrice.multiply(BigDecimal.valueOf(100 - reduction).divide(BigDecimal.valueOf(100)));
    }

    public BigDecimal getUnitPriceReductionIncluded() {
        return computeUnitPriceReductionIncluded()
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal computeUnitPriceVat() {
        return computeUnitPriceReductionIncluded()
                .multiply(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100)));
    }

    public BigDecimal computeUnitPriceVatIncluded() {
        return computeUnitPriceReductionIncluded()
                .multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal getUnitPriceVatIncluded() {
        return computeUnitPriceVatIncluded()
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal computeTotalVatExcluded() {
        return computeUnitPriceReductionIncluded()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalVatExcluded() {
        return computeTotalVatExcluded()
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal computeTotalVat() {
        return computeUnitPriceVat()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalVat() {
        return computeTotalVat()
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal computeTotalVatIncluded() {
        return computeTotalVatExcluded()
                .multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal getTotalVatIncluded() {
        return computeTotalVatIncluded()
                .setScale(2, RoundingMode.HALF_UP);
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
