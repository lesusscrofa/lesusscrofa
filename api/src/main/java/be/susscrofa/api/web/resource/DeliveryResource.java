package be.susscrofa.api.web.resource;

import be.susscrofa.api.repository.DeliveryViewRepository;
import be.susscrofa.api.view.DeliverySummaryView;
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
public class DeliveryResource {

    private final DeliveryViewRepository deliveryViewRepository;

    @GetMapping("/api/deliveries/{day}")
    public List<DeliverySummaryView> findDelivery(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate day,
                                                  @RequestParam(required = false) Long deliveryManId) {
        var deliveries = deliveryManId != null ?
                deliveryViewRepository.getAllByOrderDayAndDeliveryManId(day, deliveryManId) :
                deliveryViewRepository.getAllByOrderDay(day);

        return DeliverySummaryView.from(deliveries);
    }
}
