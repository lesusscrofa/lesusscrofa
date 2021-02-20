package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Bill;
import be.susscrofa.api.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class BillResource {

    private final BillService billService;

    @GetMapping("/api/bill/client/{clientId}/month/{month}/year/{year}")
    public Bill getBill(@PathVariable Long clientId, @PathVariable @Min(1) @Max(12) Integer month, @PathVariable Integer year) {
        var start = LocalDate.of(year, month, 1);
        var end = LocalDate.of(year, month, start.lengthOfMonth());

        return billService.getBill(clientId,
                start,
                end);
    }
}
