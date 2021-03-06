package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Bill;
import be.susscrofa.api.service.BillService;
import be.susscrofa.api.service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class BillResource {

    private final BillService billService;

    @GetMapping(value = "/api/bill/client/{clientId}/month/{month}/year/{year}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getBill(@PathVariable Long clientId, @PathVariable @Min(1) @Max(12) Integer month, @PathVariable Integer year) {
        var start = LocalDate.of(year, month, 1);
        var end = LocalDate.of(year, month, start.lengthOfMonth());

        final var headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=facture-%d-%d-%d.pdf", clientId, month, year));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(
                                new ByteArrayInputStream(
                                        billService.getBill(clientId,
                                                start,
                                                end))
                        )
                );
    }
}
