package be.susscrofa.api.repository;

import be.susscrofa.api.view.FoodOrderedView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodOrderedViewRepository extends CrudRepository<FoodOrderedView, Long> {

    List<FoodOrderedView> getAllByOrderDay(LocalDate day);
}
