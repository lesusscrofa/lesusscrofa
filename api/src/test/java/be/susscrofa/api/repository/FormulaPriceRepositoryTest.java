package be.susscrofa.api.repository;

import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.model.Formula;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class FormulaPriceRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private FormulaPriceRepository formulaPriceRepository;

    @Test
    @Rollback
    @Transactional
    void findActives() {
        formulaPriceRepository.deleteAll();

        var now = LocalDate.of(2020, 12, 15);

        var bpNotActiveAnymore = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 1))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        var bpNotYetActive = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 16))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        var bps = List.of(FormulaPrice
                    .builder()
                    .start(LocalDate.of(2020, 12, 10))
                    .formula(Formula.MENU)
                    .price(BigDecimal.valueOf(10.3))
                    .build(),
                FormulaPrice
                    .builder()
                    .start(LocalDate.of(2020, 12, 10))
                    .formula(Formula.DISH)
                    .price(BigDecimal.valueOf(10.3))
                    .build(),
                FormulaPrice
                    .builder()
                    .start(LocalDate.of(2020, 12, 15))
                    .formula(Formula.DESSERT)
                    .price(BigDecimal.valueOf(10.3))
                    .build());

        formulaPriceRepository.save(bpNotActiveAnymore);
        formulaPriceRepository.save(bpNotYetActive);
        formulaPriceRepository.saveAll(bps);

        var result = formulaPriceRepository.findActives(now);

        assertThat(result).containsExactlyInAnyOrderElementsOf(bps);
    }

    @Test
    @Rollback
    @Transactional
    void findActiveForMealType() {
        formulaPriceRepository.deleteAll();

        var bp1 = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 1))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        var bp2 = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 10))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        var bp3 = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 10))
                .formula(Formula.DISH)
                .price(BigDecimal.valueOf(10.3))
                .build();

        var bp4 = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 16))
                .formula(Formula.DISH)
                .price(BigDecimal.valueOf(10.3))
                .build();

        formulaPriceRepository.save(bp1);
        formulaPriceRepository.save(bp2);
        formulaPriceRepository.save(bp3);
        formulaPriceRepository.save(bp4);

        var res1 = formulaPriceRepository
                .findByFormulaAndStartLessThanEqual(
                        Formula.MENU,
                        LocalDate.of(2020, 12, 15),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "start"));
        var res2 = formulaPriceRepository
                .findByFormulaAndStartLessThanEqual(
                        Formula.DISH,
                        LocalDate.of(2020, 12, 15),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "start"));

        assertThat(res1).isEqualTo(List.of(bp2));
        assertThat(res2).isEqualTo(List.of(bp3));
    }

    @ParameterizedTest
    @CsvSource({
            "2020-12-02, true",
            "2020-11-29, false",
            "2020-12-01, true"
    })
    @Rollback
    @Transactional
    void existsActiveForMealType(LocalDate start, boolean expectedResult) {
        formulaPriceRepository.deleteAll();

        var bp = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 1))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        formulaPriceRepository.save(bp);

        var result = formulaPriceRepository.findByFormulaAndStartLessThanEqual(Formula.MENU, start, PageRequest.of(0, 1, Sort.Direction.DESC, "start"));

        assertThat(result.size() > 0).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "2020-12-02, false",
            "2020-12-10, true",
            "2020-12-11, true"
    })
    @Rollback
    @Transactional
    void existsActiveForMealType_nullEndDate(LocalDate start, boolean expectedResult) {
        formulaPriceRepository.deleteAll();

        var bp = FormulaPrice
                .builder()
                .start(LocalDate.of(2020, 12, 10))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        formulaPriceRepository.save(bp);

        var result = formulaPriceRepository.findByFormulaAndStartLessThanEqual(Formula.MENU, start, PageRequest.of(0, 1, Sort.Direction.DESC, "start"));

        assertThat(result.size() > 0).isEqualTo(expectedResult);
    }
}
