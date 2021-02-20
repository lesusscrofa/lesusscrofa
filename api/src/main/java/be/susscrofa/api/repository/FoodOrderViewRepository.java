package be.susscrofa.api.repository;

import be.susscrofa.api.view.FoodOrderView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodOrderViewRepository extends CrudRepository<FoodOrderView, Long> {

    List<FoodOrderView> getAllByOrderDay(LocalDate day);
}
