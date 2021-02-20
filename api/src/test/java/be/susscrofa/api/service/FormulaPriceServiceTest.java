package be.susscrofa.api.service;

import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.repository.FormulaPriceRepository;
import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.service.exception.NoActivePriceForFormulaException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormulaPriceServiceTest {

    @InjectMocks
    private FormulaPriceService formulaPriceService;

    @Mock
    private FormulaPriceRepository formulaPriceRepository;

    @Test
    void getActives() {
        formulaPriceService.findActives();

        verify(formulaPriceRepository).findActives(Mockito.any());
    }

    @Test
    void getActiveFormula() {
        var formula = Formula.MENU;

        var fp = FormulaPrice.builder().build();

        when(formulaPriceRepository.findByFormulaAndStartLessThanEqual(eq(formula),
                Mockito.any(),
                eq(PageRequest.of(0, 1, Sort.Direction.DESC, "start"))))
                    .thenReturn(List.of(fp))
                ;

        var result = formulaPriceService.getActive(formula);

        Assertions.assertThat(result).isEqualTo(fp);
    }

    @Test
    void getActiveFormula_notFound() {
        var formula = Formula.MENU;

        when(formulaPriceRepository.findByFormulaAndStartLessThanEqual(eq(formula),
                Mockito.any(),
                eq(PageRequest.of(0, 1, Sort.Direction.DESC, "start"))))
                .thenReturn(List.of())
        ;

        assertThatThrownBy(() -> formulaPriceService.getActive(formula))
            .isInstanceOf(NoActivePriceForFormulaException.class);
    }

    @Test
    void create() {
        var bp = FormulaPrice
                .builder()
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findByFormulaAndStartEquals(Formula.MENU, bp.getStart()))
                .thenReturn(Optional.empty());

        formulaPriceService.create(bp);

        verify(formulaPriceRepository).save(bp);
    }

    @Test
    void create_alreadyExist() {
        var bp = FormulaPrice
                .builder()
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findByFormulaAndStartEquals(Formula.MENU, bp.getStart()))
                .thenReturn(Optional.of(FormulaPrice.builder().build()));

        assertThatThrownBy(() -> formulaPriceService.create(bp))
            .isInstanceOf(EntityAlreadyExistException.class);
    }

    @Test
    void update() {
        var bp = FormulaPrice
                .builder()
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findByFormulaAndStartEquals(Formula.MENU, bp.getStart()))
                .thenReturn(Optional.empty());

        when(formulaPriceRepository.existsById(bp.getId()))
                .thenReturn(true);

        formulaPriceService.update(bp);

        verify(formulaPriceRepository).save(bp);
    }

    @Test
    void update_notFound() {
        var bp = FormulaPrice
                .builder()
                .id(1L)
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.existsById(bp.getId()))
                .thenReturn(false);

        assertThatThrownBy(() -> formulaPriceService.update(bp))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete() {
        var bp = FormulaPrice
                .builder()
                .id(1L)
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        var stillActiveBp = FormulaPrice
                .builder()
                .id(2L)
                .start(LocalDate.now().minusDays(1))
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findById(bp.getId()))
                .thenReturn(Optional.of(bp));

        when(formulaPriceRepository.findByFormula(bp.getFormula()))
                .thenReturn(List.of(bp, stillActiveBp));

        formulaPriceService.delete(bp.getId());

        verify(formulaPriceRepository).delete(bp);
    }

    @Test
    void delete_notFound() {
        var bp = FormulaPrice
                .builder()
                .id(1L)
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findById(bp.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> formulaPriceService.delete(bp.getId()))
            .isInstanceOf(EntityNotFoundException.class);

        verify(formulaPriceRepository, never()).delete(bp);
    }

    @Test
    void delete_notMoreActivePriceForFormula() {
        var bp = FormulaPrice
                .builder()
                .id(1L)
                .start(LocalDate.now())
                .formula(Formula.MENU)
                .build();

        var notActiveBp = FormulaPrice
                .builder()
                .id(2L)
                .start(LocalDate.now().plusDays(1))
                .formula(Formula.MENU)
                .build();

        when(formulaPriceRepository.findById(bp.getId()))
                .thenReturn(Optional.of(bp));

        when(formulaPriceRepository.findByFormula(bp.getFormula()))
                .thenReturn(List.of(bp, notActiveBp));

        assertThatThrownBy(() -> formulaPriceService.delete(bp.getId()))
                .isInstanceOf(NoActivePriceForFormulaException.class);

        verify(formulaPriceRepository, never()).delete(bp);
    }
}
