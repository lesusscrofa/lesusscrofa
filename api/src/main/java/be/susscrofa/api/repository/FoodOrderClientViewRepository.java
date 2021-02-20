package be.susscrofa.api.repository;

import be.susscrofa.api.view.ClientWithFoodOrderView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface FoodOrderClientViewRepository extends CrudRepository<ClientWithFoodOrderView, Long> {

    @Query("SELECT DISTINCT c FROM ClientWithFoodOrderView c JOIN FETCH c.foodsOrders fo WHERE fo.id.orderDay = :day")
    List<ClientWithFoodOrderView> getAllByOrderDay(LocalDate day);

    @Query("SELECT DISTINCT c FROM ClientWithFoodOrderView c JOIN FETCH c.foodsOrders fo" +
            " WHERE fo.id.orderDay = :day and c.deliveryManId = :deliveryManId")
    List<ClientWithFoodOrderView> getAllByOrderDayAndDeliveryManId(LocalDate day, Long deliveryManId);
}
