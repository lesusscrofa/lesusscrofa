package be.susscrofa.api.view;

import be.susscrofa.api.model.ServiceEnum;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrderedView {

    @Id
    @NotNull
    private long foodId;

    @NotNull
    private String foodName;

    @NotNull
    private LocalDate orderDay;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceEnum foodService;

    @NotNull
    private int quantity;
}
