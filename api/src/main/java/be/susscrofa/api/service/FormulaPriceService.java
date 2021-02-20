package be.susscrofa.api.service;

import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.repository.FormulaPriceRepository;
import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.service.exception.NoActivePriceForFormulaException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FormulaPriceService {

    protected static final String BILLING_PRICE_DONT_EXIST = "Billing price with id %d not found";

    protected static final String BILLING_PRICE_ALREADY_EXIST = "There is already an active billing price for that mealType with id %d";

    protected static final String NO_ACTIVE_PRICE_FOR_FORMULA_DELETE = "There is no active price for this formula if you delete price with id %d";

    protected static final String NO_ACTIVE_PRICE_FOR_FORMULA = "There is no active price for this formula";

    private final FormulaPriceRepository formulaPriceRepository;

    public List<FormulaPrice> findActives() {
        return formulaPriceRepository.findActives(LocalDate.now());
    }

    public FormulaPrice getActive(Formula formula) {
        return formulaPriceRepository.findByFormulaAndStartLessThanEqual(
                formula,
                LocalDate.now(),
                PageRequest.of(0, 1, Sort.Direction.DESC, "start"))
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new NoActivePriceForFormulaException(NO_ACTIVE_PRICE_FOR_FORMULA));
    }

    public List<FormulaPrice> find(@NotNull Formula formula) {
        return formulaPriceRepository.findByFormula(formula);
    }

    public FormulaPrice create(FormulaPrice formulaPrice) {
        return save(formulaPrice);
    }

    public FormulaPrice update(FormulaPrice formulaPrice) {
        if(!formulaPriceRepository.existsById(formulaPrice.getId())) {
            throw new EntityNotFoundException(String.format(BILLING_PRICE_DONT_EXIST, formulaPrice.getId()));
        }

        return save(formulaPrice);
    }

    public void delete(long id) {
        var formulaPrice = formulaPriceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(BILLING_PRICE_DONT_EXIST, id)));

        formulaPriceRepository.findByFormula(formulaPrice.getFormula())
                .stream()
                .filter(fp -> !fp.getId().equals(formulaPrice.getId()))
                .filter(fp -> fp.getStart().isBefore(LocalDate.now()) || fp.getStart().isEqual(LocalDate.now()))
                .findAny()
                .orElseThrow(() -> new NoActivePriceForFormulaException(String.format(NO_ACTIVE_PRICE_FOR_FORMULA_DELETE, formulaPrice.getId())));

        formulaPriceRepository.delete(formulaPrice);
    }

    private FormulaPrice save(FormulaPrice formulaPrice) {
        formulaPriceRepository
                .findByFormulaAndStartEquals(formulaPrice.getFormula(), formulaPrice.getStart())
                .filter(bp -> formulaPrice.getId() == null || formulaPrice.getId().equals(bp.getId()))
                .ifPresent(bp -> {
                    throw new EntityAlreadyExistException(String.format(BILLING_PRICE_ALREADY_EXIST, bp.getId()));
                });

        return formulaPriceRepository.save(formulaPrice);
    }
}
