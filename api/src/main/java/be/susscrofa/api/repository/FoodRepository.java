package be.susscrofa.api.repository;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findAllByService(ServiceEnum service);

    List<Food> findAllByEndGreaterThanEqualAndStartLessThanEqualAndService(LocalDate startDate, LocalDate endDate, ServiceEnum service);

    Optional<Food> findByStartAndService(LocalDate startDate, ServiceEnum service);

    List<Food> findAllByEndGreaterThanEqualAndStartLessThanEqual(LocalDate startDate, LocalDate endDate);
}
