package be.susscrofa.api.view;

import be.susscrofa.api.model.Formula;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderWithPriceView {

    @Id
    private Long id;

    private String name;

    private Long clientId;

    private LocalDate day;

    @Enumerated(EnumType.STRING)
    private Formula formula;

    private BigDecimal price;

    private Integer vat;

    private Integer quantity;

    private String unit;
}
