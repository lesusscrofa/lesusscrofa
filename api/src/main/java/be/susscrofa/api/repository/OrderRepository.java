package be.susscrofa.api.repository;

import java.time.LocalDate;
import java.util.List;

import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByClientIdAndDay(Long clientId, LocalDate day);

	List<Order> findByClientIdAndDayAndFormula(Long clientId, LocalDate day, Formula formula);

	boolean existsByClientIdAndDayAndFormula(Long clientId, LocalDate day, Formula formula);
}
