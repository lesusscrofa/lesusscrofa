package be.susscrofa.api.repository;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findAllByService(ServiceEnum service);

    @Query("SELECT f FROM Food f WHERE (f.end >= :startDate OR f.end is null) AND f.start <= :endDate AND f.service = :service")
    List<Food> findAll(LocalDate startDate, LocalDate endDate, ServiceEnum service);

    @Query("SELECT f FROM Food f" +
            " WHERE (f.end >= :startDate OR f.end is null)" +
            " AND f.start <= :endDate AND f.service = :service" +
            " AND lower(f.name) like lower(concat('%', :partialName,'%'))")
    List<Food> findAll(LocalDate startDate, LocalDate endDate, ServiceEnum service, String partialName);

    Optional<Food> findByStartAndService(LocalDate startDate, ServiceEnum service);

    List<Food> findAllByEndGreaterThanEqualAndStartLessThanEqual(LocalDate startDate, LocalDate endDate);
}
