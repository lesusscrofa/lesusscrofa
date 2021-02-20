package be.susscrofa.api.repository;

import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.model.Formula;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormulaPriceRepository extends PagingAndSortingRepository<FormulaPrice, Long> {

    @Query(nativeQuery = true, value = "SELECT fp.* FROM formula_price fp " +
            "INNER JOIN (SELECT fpm.formula, MAX(fpm.start_date) AS min_start_date" +
            "   FROM formula_price fpm " +
            "   WHERE fpm.start_date <= :now" +
            "   GROUP BY fpm.formula) fp2 ON fp.formula = fp2.formula and fp.start_date = fp2.min_start_date")
    List<FormulaPrice> findActives(@Param("now") LocalDate now);

    List<FormulaPrice> findByFormula(Formula formula);

    List<FormulaPrice> findByFormulaAndStartLessThanEqual(Formula formula, LocalDate now, Pageable pageable);

    Optional<FormulaPrice> findByFormulaAndStartEquals(Formula formula, LocalDate start);
}
