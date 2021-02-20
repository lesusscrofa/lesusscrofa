package be.susscrofa.api.repository;

import be.susscrofa.api.model.OrderSummary;
import be.susscrofa.api.view.OrderWithPriceView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderWithPriceRepository extends CrudRepository<OrderWithPriceView, Long> {

    @Query(value = "select new be.suscrofa.api.model.OrderSummary(op.id, op.name, op.formula, op.price, op.vat, cast(sum(op.quantity) as int), 0, op.unit) " +
            "from OrderWithPriceView op " +
            "where op.clientId = :clientId and op.day between :start and :end " +
            "group by op.id, op.name, op.formula, op.price, op.vat, op.unit")
    List<OrderSummary> findOrdersSummaries(long clientId, LocalDate start, LocalDate end);
}
