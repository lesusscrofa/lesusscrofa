package be.susscrofa.api.repository;

import be.susscrofa.api.view.FoodOrderedView;
import be.susscrofa.api.model.FoodOrderData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class FoodOrderedViewRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    FoodOrderedViewRepository foodOrderedViewRepository;

    @Autowired
    private DeliveryManRepository deliveryManRepository;

    @Autowired
    private DeliveryZoneRepository deliveryZoneRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void testFindAllByNameAndSurname() {
        var data = new FoodOrderData();

        var order1WithDay1Soup1Dish1Dessert1 = data.order1WithDay1Soup1Dish1Dessert1Client1;
        var order2WithDay1Dish1 = data.order2WithDay1Dish1Client2;
        var order3WithDay2Dish2Client1 = data.order3WithDay2Dish2Client1;
        var soup1 = data.soup1;
        var dish1 = data.dish1;
        var dessert1 = data.dessert1;
        var dish2 = data.dish2;

        var deliveryMan = deliveryManRepository.save(data.deliveryMan);

        var deliveryZone = data.deliveryZone;
        deliveryZone.setDeliveryManId(deliveryMan.getId());
        deliveryZone = deliveryZoneRepository.save(deliveryZone);

        var client1 = data.client1;
        client1.setDeliveryZoneId(deliveryZone.getId());
        client1 = clientRepository.save(client1);
        var client2 = data.client2;
        client2.setDeliveryZoneId(deliveryZone.getId());
        client2 = clientRepository.save(client2);

        soup1 = foodRepository.save(soup1);
        dish1 = foodRepository.save(dish1);
        dessert1 = foodRepository.save(dessert1);
        dish2 = foodRepository.save(dish2);

        order1WithDay1Soup1Dish1Dessert1.setClientId(client1.getId());
        order1WithDay1Soup1Dish1Dessert1.setSoupId(soup1.getId());
        order1WithDay1Soup1Dish1Dessert1.setDishId(dish1.getId());
        order1WithDay1Soup1Dish1Dessert1.setDessertId(dessert1.getId());
        order1WithDay1Soup1Dish1Dessert1 = orderRepository.save(order1WithDay1Soup1Dish1Dessert1);

        order2WithDay1Dish1.setClientId(client2.getId());
        order2WithDay1Dish1.setDishId(dish1.getId());
        order2WithDay1Dish1 = orderRepository.save(order2WithDay1Dish1);

        order3WithDay2Dish2Client1.setClientId(client1.getId());
        order3WithDay2Dish2Client1.setDishId(dish2.getId());
        orderRepository.save(order3WithDay2Dish2Client1);

        var foodsOrders = foodOrderedViewRepository.getAllByOrderDay(data.day1);
        assertThat(foodsOrders.size()).isEqualTo(3);
        Assertions.assertThat(foodsOrders).containsExactlyInAnyOrder(
                FoodOrderedView.builder()
                        .foodId(soup1.getId())
                        .foodName(soup1.getName())
                        .orderDay(order1WithDay1Soup1Dish1Dessert1.getDay())
                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity())
                        .foodService(soup1.getService())
                        .build(),
                FoodOrderedView.builder()
                        .foodId(dish1.getId())
                        .foodName(dish1.getName())
                        .orderDay(order1WithDay1Soup1Dish1Dessert1.getDay())
                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity()
                                    + order2WithDay1Dish1.getQuantity())
                        .foodService(dish1.getService())
                        .build(),
                FoodOrderedView.builder()
                        .foodId(dessert1.getId())
                        .foodName(dessert1.getName())
                        .orderDay(order1WithDay1Soup1Dish1Dessert1.getDay())
                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity())
                        .foodService(dessert1.getService())
                        .build()
        );
    }


}
