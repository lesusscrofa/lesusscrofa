package be.susscrofa.api.web.resource;

import be.susscrofa.api.repository.FoodOrderClientViewRepository;
import be.susscrofa.api.repository.FoodOrderViewRepository;
import be.susscrofa.api.view.ClientWithFoodOrderFlatView;
import be.susscrofa.api.view.ClientWithFoodOrderView;
import be.susscrofa.api.view.FoodOrderView;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_deliveryman')")
@RestController
public class FoodOrderResource {

    private final FoodOrderViewRepository foodOrderViewRepository;

    private final FoodOrderClientViewRepository foodOrderClientViewRepository;

    @GetMapping("/api/orders/{day}/foods")
    public List<FoodOrderView> findFoodsOrders(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate day) {
        return foodOrderViewRepository.getAllByOrderDay(day);
    }

    @GetMapping("/api/orders/{day}/foods/byClient")
    public List<ClientWithFoodOrderView> findFoodsOrdersByClient(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate day) {
        return foodOrderClientViewRepository.getAllByOrderDay(day);
    }

    @GetMapping("/api/orders/{day}/foods/byClient/flat")
    public List<ClientWithFoodOrderFlatView> findFoodsOrdersByClientFlat(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate day,
                                                                         @RequestParam(required = false) Long deliveryManId) {
        var deliveries = deliveryManId != null ?
                foodOrderClientViewRepository.getAllByOrderDayAndDeliveryManId(day, deliveryManId) :
                foodOrderClientViewRepository.getAllByOrderDay(day);

        return ClientWithFoodOrderFlatView.from(deliveries);
    }
}
