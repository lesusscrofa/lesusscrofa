package be.susscrofa.api.repository;

import be.susscrofa.api.view.DeliveryView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryViewRepository extends CrudRepository<DeliveryView, Long> {

    @Query("SELECT DISTINCT c FROM DeliveryView c JOIN FETCH c.foodsOrders fo WHERE fo.id.orderDay = :day")
    List<DeliveryView> getAllByOrderDay(LocalDate day);

    @Query("SELECT DISTINCT c FROM DeliveryView c JOIN FETCH c.foodsOrders fo" +
            " WHERE fo.id.orderDay = :day and c.deliveryManId = :deliveryManId")
    List<DeliveryView> getAllByOrderDayAndDeliveryManId(LocalDate day, Long deliveryManId);
}
