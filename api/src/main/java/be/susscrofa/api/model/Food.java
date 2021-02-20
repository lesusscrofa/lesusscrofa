package be.susscrofa.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Food {

    public static final String FOOD_DATE_INVALID = "Start date and end date invalid";

    public static final String FOOD_PRICE_MISSING_OR_INVALID = "Price is mandatory and must be > 0 for OTHER service";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "name is missing")
    private String name;

    @NotNull(message = "service is missing")
    @Enumerated(EnumType.STRING)
    private ServiceEnum service;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    @Min(0)
    private BigDecimal price;

    @Min(0)
    @Max(100)
    private int vat;

    @Size(max = 10)
    private String unit;

    @JsonIgnore
    public boolean isMainDish() {
        return ServiceEnum.DISH.equals(service);
    }

    @JsonIgnore
    public boolean isAlternativeDish() {
        return ServiceEnum.ALTERNATIVE_DISH.equals(service);
    }

    @JsonIgnore
    public boolean isSoup() {
        return ServiceEnum.SOUP.equals(service);
    }

    @JsonIgnore
    public boolean isDessert() {
        return ServiceEnum.DESSERT.equals(service);
    }

    @JsonIgnore
    public void isValid() {
        if(!isDatesValid()) {
            throw new IllegalArgumentException(FOOD_DATE_INVALID);
        }

        if(service.equals(ServiceEnum.OTHER)) {
            if(price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw  new IllegalArgumentException(FOOD_PRICE_MISSING_OR_INVALID);
            }
        }
    }

    @JsonIgnore
    private boolean isDatesValid() {
        if(service.equals(ServiceEnum.SOUP) || service.equals(ServiceEnum.DISH) || service.equals(ServiceEnum.DESSERT)) {
            return start != null && start.equals(end);
        }
        else if(service.equals(ServiceEnum.ALTERNATIVE_DISH)) {
            return start != null && end != null
                    && (start.isBefore(end) || start.equals(end));
        }
        else {
            return start == null || end == null || !start.isAfter(end);
        }
    }
}
