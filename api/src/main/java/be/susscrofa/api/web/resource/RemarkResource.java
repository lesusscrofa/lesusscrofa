package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Remark;
import be.susscrofa.api.service.RemarkService;
import be.susscrofa.api.view.DailyOrderRemarkView;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_deliveryman')")
@RestController
public class RemarkResource {

    private final RemarkService remarkService;

    @GetMapping("/api/orders/{day}/remarks")
    public List<DailyOrderRemarkView> findRemarks(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return  remarkService.findRemarks(day);
    }

    @GetMapping("/api/clients/{clientId}/remarks")
    public Remark getRemarkForClient(@PathVariable Long clientId) {
        return remarkService.getRemark(clientId, null);
    }

    @GetMapping("/api/clients/{clientId}/remarks/{day}")
    public Remark getRemarkForClient(@PathVariable Long clientId,
                                     @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return remarkService.getRemark(clientId, day);
    }

    @PutMapping("/api/clients/{clientId}/remarks/{day}")
    public Remark update(@PathVariable Long clientId,
                         @PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                         @RequestBody Remark remark) {
        return remarkService.save(clientId, day, remark.getMessage());
    }

    @DeleteMapping("/api/clients/{clientId}/remarks/{day}")
    public Remark deleteRemarkForClient(@PathVariable Long clientId,
                                     @PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return remarkService.delete(clientId, day);
    }
}
