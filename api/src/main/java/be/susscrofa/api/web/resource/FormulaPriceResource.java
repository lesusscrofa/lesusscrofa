package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.service.FormulaPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class FormulaPriceResource {

    private final FormulaPriceService formulaPriceService;

    @GetMapping("/api/formulas/prices/actives")
    public List<FormulaPrice> getActives() {
        return this.formulaPriceService.findActives();
    }

    @GetMapping("/api/formulas/prices")
    public List<FormulaPrice> getFormulas(@NotNull @RequestParam Formula formula) {
        return this.formulaPriceService.find(formula);
    }

    @PostMapping("/api/formulas/prices")
    @ResponseStatus(code = HttpStatus.CREATED)
    public FormulaPrice create(@RequestBody @Valid FormulaPrice formulaPrice) {
        return this.formulaPriceService.create(formulaPrice);
    }

    @PutMapping("/api/formulas/prices/{bpId}")
    public FormulaPrice update(@PathVariable long bpId, @RequestBody @Valid FormulaPrice formulaPrice) {
        formulaPrice.setId(bpId);

        return this.formulaPriceService.update(formulaPrice);
    }

    @DeleteMapping("/api/formulas/prices/{bpId}")
    public void delete(@PathVariable int bpId) {
        this.formulaPriceService.delete(bpId);
    }
}
