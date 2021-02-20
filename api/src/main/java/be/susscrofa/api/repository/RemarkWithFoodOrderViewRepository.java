package be.susscrofa.api.repository;

import be.susscrofa.api.view.DailyOrderRemarkView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RemarkWithFoodOrderViewRepository extends CrudRepository<DailyOrderRemarkView, Long> {

    @EntityGraph(attributePaths = "foodsOrders")
    List<DailyOrderRemarkView> findAllByDay(LocalDate day);
}
