package be.susscrofa.api.web.resource;

import be.susscrofa.api.repository.FoodOrderedViewRepository;
import be.susscrofa.api.view.FoodOrderedView;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_deliveryman')")
@RestController
public class OrderedFoodResource {

    private final FoodOrderedViewRepository foodOrderedViewRepository;

    @GetMapping("/api/orders/{day}/foods")
    public List<FoodOrderedView> findFoodOrdered(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate day) {
        return foodOrderedViewRepository.getAllByOrderDay(day);
    }
}
