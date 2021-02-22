package be.susscrofa.api.repository;

import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.view.DeliverySummaryView;
import be.susscrofa.api.view.DeliveryView;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliverySummaryViewTest extends AbstractRepositoryTest {

    @Test
    public void createClientWithFoodOrderFlatViewTest() {
        var day1 = LocalDate.of(2020, 12, 1);
        var day2 = LocalDate.of(2020, 12, 2);

        var client1Id = 1L;
        var client1FirstName = "Maxime";
        var client1LastName = "Homme";

        var client2Id = 2L;
        var client2FirstName = "Sarah";
        var client2LastName = "Fraichi";

        var client1WithFoods = DeliveryView
                .builder()
                .id(client1Id)
                .firstName(client1FirstName)
                .lastName(client1LastName)
                .foodsOrders(List.of(
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day1, 1L))
                                .foodName("Brocoli")
                                .quantity(3)
                                .foodService(ServiceEnum.SOUP)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day1, 2L))
                                .foodName("Boulette tomate")
                                .quantity(3)
                                .foodService(ServiceEnum.DISH)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day1, 3L))
                                .foodName("Mousse chocolat")
                                .quantity(5)
                                .foodService(ServiceEnum.DESSERT)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day2, 4L))
                                .foodName("Cresson")
                                .quantity(3)
                                .foodService(ServiceEnum.SOUP)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day2, 5L))
                                .foodName("Lasagne")
                                .quantity(3)
                                .foodService(ServiceEnum.DISH)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client1Id, day2, 6L))
                                .foodName("Tiramissu")
                                .quantity(5)
                                .foodService(ServiceEnum.DESSERT)
                                .build()
                ))
                .build();

        var client2WithFoods = DeliveryView
                .builder()
                .id(client2Id)
                .firstName(client2FirstName)
                .lastName(client2LastName)
                .foodsOrders(List.of(
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client2Id, day1, 1L))
                                .foodName("Brocoli")
                                .quantity(2)
                                .foodService(ServiceEnum.SOUP)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client2Id, day1, 2L))
                                .foodName("Boulette tomate")
                                .quantity(2)
                                .foodService(ServiceEnum.DISH)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client2Id, day1, 3L))
                                .foodName("Crudités")
                                .quantity(3)
                                .foodService(ServiceEnum.OTHER)
                                .build(),
                        DeliveryView.FoodOrderedByClientView
                                .builder()
                                .id(new DeliveryView.FoodOrderedByClientView.Id(client2Id, day1, 4L))
                                .foodName("Plus de crudités")
                                .quantity(1)
                                .foodService(ServiceEnum.OTHER)
                                .build()
                ))
                .build();

        var result = DeliverySummaryView.from(List.of(client1WithFoods, client2WithFoods));
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsAnyElementsOf(
                List.of(
                        DeliverySummaryView
                            .builder()
                            .day(day1)
                            .id(client1Id)
                            .firstName(client1FirstName)
                            .lastName(client1LastName)
                            .soupName("Brocoli")
                            .soupQuantity(3)
                            .dishName("Boulette tomate")
                            .dishQuantity(3)
                            .dessertName("Mousse au chocolat")
                            .dessertQuantity(5)
                            .others(List.of())
                            .build(),
                        DeliverySummaryView
                            .builder()
                            .day(day2)
                            .id(client1Id)
                            .firstName(client1FirstName)
                            .lastName(client1LastName)
                            .soupName("Cresson")
                            .soupQuantity(3)
                            .dishName("Lasagne")
                            .dishQuantity(3)
                            .dessertName("Tiramissu")
                            .dessertQuantity(5)
                            .others(List.of())
                            .build(),
                        DeliverySummaryView
                            .builder()
                            .day(day1)
                            .id(client2Id)
                            .firstName(client2FirstName)
                            .lastName(client2LastName)
                            .soupName("Brocoli")
                            .soupQuantity(2)
                            .dishName("Boulette tomate")
                            .dishQuantity(2)
                            .others(List.of(
                                    DeliverySummaryView.OtherFoodDeliveryView
                                            .builder()
                                            .name("Crudités")
                                            .quantity(3)
                                            .build(),
                                    DeliverySummaryView.OtherFoodDeliveryView
                                            .builder()
                                            .name("Plus de crudités")
                                            .quantity(1)
                                            .build()
                            ))
                            .build()
                )
        );
    }
}
