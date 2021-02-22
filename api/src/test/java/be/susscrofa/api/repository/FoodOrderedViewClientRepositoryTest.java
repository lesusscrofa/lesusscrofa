package be.susscrofa.api.repository;

import be.susscrofa.api.model.FoodOrderData;
import be.susscrofa.api.view.DeliveryView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FoodOrderedViewClientRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private DeliveryViewRepository deliveryViewRepository;

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

        var soup1 = foodRepository.save(data.soup1);
        var dish1 = foodRepository.save(data.dish1);
        var dessert1 = foodRepository.save(data.dessert1);
        var dish2 = foodRepository.save(data.dish2);

        var order1WithDay1Soup1Dish1Dessert1 = data.order1WithDay1Soup1Dish1Dessert1Client1;
        order1WithDay1Soup1Dish1Dessert1.setClientId(client1.getId());
        order1WithDay1Soup1Dish1Dessert1.setSoupId(soup1.getId());
        order1WithDay1Soup1Dish1Dessert1.setDishId(dish1.getId());
        order1WithDay1Soup1Dish1Dessert1.setDessertId(dessert1.getId());
        order1WithDay1Soup1Dish1Dessert1 = orderRepository.save(order1WithDay1Soup1Dish1Dessert1);

        var order2WithDay1Dish1 = data.order2WithDay1Dish1Client2;
        order2WithDay1Dish1.setClientId(client2.getId());
        order2WithDay1Dish1.setDishId(dish1.getId());
        order2WithDay1Dish1 = orderRepository.save(order2WithDay1Dish1);

        var order3WithDay2Dish2Client1 = data.order3WithDay2Dish2Client1;
        order3WithDay2Dish2Client1.setClientId(client1.getId());
        order3WithDay2Dish2Client1.setDishId(dish2.getId());
        orderRepository.save(order3WithDay2Dish2Client1);

        var order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity = data.order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity;
        order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity.setClientId(client1.getId());
        order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity.setSoupId(soup1.getId());
        order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity.setDishId(dish1.getId());
        order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity.setDessertId(dessert1.getId());
        orderRepository.save(order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity);

        var foodsOrders = deliveryViewRepository.getAllByOrderDay(data.day1);
        assertThat(foodsOrders.size()).isEqualTo(2);
        assertThat(foodsOrders)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                DeliveryView.builder()
                        .id(client1.getId())
                        .firstName(client1.getFirstName())
                        .lastName(client1.getLastName())
                        .deliveryPhone(client1.getDeliveryPhone())
                        .deliveryStreet(client1.getDeliveryStreet())
                        .deliveryZoneId(client1.getDeliveryZoneId())
                        .deliveryZoneName(deliveryZone.getName())
                        .deliveryZipCode(client1.getDeliveryZipCode())
                        .deliveryCity(client1.getDeliveryCity())
                        .deliveryManId(deliveryMan.getId())
                        .deliveryManFirstName(deliveryMan.getFirstName())
                        .deliveryManLastName(deliveryMan.getLastName())
                        .deliveryPosition(client1.getDeliveryPosition())
                        .foodsOrders(
                                List.of(DeliveryView.FoodOrderedByClientView.builder()
                                        .id(new DeliveryView.FoodOrderedByClientView.
                                                Id(client1.getId(), order1WithDay1Soup1Dish1Dessert1.getDay(), soup1.getId()))
                                        .foodName(soup1.getName())
                                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity())
                                        .foodService(soup1.getService())
                                        .build(),
                                DeliveryView.FoodOrderedByClientView.builder()
                                        .id(new DeliveryView.FoodOrderedByClientView.
                                                Id(client1.getId(), order1WithDay1Soup1Dish1Dessert1.getDay(), dish1.getId()))
                                        .foodName(dish1.getName())
                                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity())
                                        .foodService(dish1.getService())
                                        .build(),
                                DeliveryView.FoodOrderedByClientView.builder()
                                        .id(new DeliveryView.FoodOrderedByClientView.
                                                Id(client1.getId(), order1WithDay1Soup1Dish1Dessert1.getDay(), dessert1.getId()))
                                        .foodName(dessert1.getName())
                                        .quantity(order1WithDay1Soup1Dish1Dessert1.getQuantity())
                                        .foodService(dessert1.getService())
                                        .build())
                        )
                        .build(),
                DeliveryView.builder()
                        .id(client2.getId())
                        .firstName(client2.getFirstName())
                        .lastName(client2.getLastName())
                        .deliveryPhone(client2.getDeliveryPhone())
                        .deliveryStreet(client2.getDeliveryStreet())
                        .deliveryZoneId(client2.getDeliveryZoneId())
                        .deliveryZoneName(deliveryZone.getName())
                        .deliveryZipCode(client2.getDeliveryZipCode())
                        .deliveryCity(client1.getDeliveryCity())
                        .deliveryManId(deliveryMan.getId())
                        .deliveryManFirstName(deliveryMan.getFirstName())
                        .deliveryManLastName(deliveryMan.getLastName())
                        .deliveryPosition(client2.getDeliveryPosition())
                        .foodsOrders(
                                List.of(DeliveryView.FoodOrderedByClientView.builder()
                                        .id(new DeliveryView.FoodOrderedByClientView.
                                                Id(client2.getId(), order2WithDay1Dish1.getDay(), dish1.getId()))
                                                .foodName(dish1.getName())
                                                .quantity(order2WithDay1Dish1.getQuantity())
                                                .foodService(dish1.getService())
                                                .build())
                        )
                        .build()
        );
    }
}
