package be.susscrofa.api.model;

import java.time.LocalDate;

public class FoodOrderData {

    public final LocalDate day1 = LocalDate.of(2020, 12, 2);

    public final LocalDate day2 = LocalDate.of(2020, 12, 3);

    public final DeliveryMan deliveryMan = DeliveryMan.builder()
            .firstName("first")
            .lastName("last")
            .build();

    public final DeliveryZone deliveryZone = DeliveryZone.builder()
            .id(1L)
            .name("Zone1")
            .deliveryManId(1L)
            .build();

    public final Client client1 = Client.builder()
            .id(1L)
            .firstName("Axel")
            .lastName("Aire")
            .deliveryZoneId(1L)
            .deliveryPosition(1)
            .billingStreet("billingStreet")
            .billingZipCode(1200)
            .billingCity("city")
            .billingPhone("billingPhone")
            .build();

    public final Client client2 = Client.builder()
            .id(2L)
            .firstName("Maxime")
            .lastName("Homme")
            .deliveryZoneId(1L)
            .deliveryPosition(2)
            .build();

    public final Food soup1 = Food.builder()
            .id(1L)
            .name("")
            .service(ServiceEnum.SOUP)
            .start(LocalDate.of(2020, 12, 1))
            .end(LocalDate.of(2020, 12, 1))
            .build();

    public final Food dish1 = Food.builder()
            .id(2L)
            .name("")
            .service(ServiceEnum.DISH)
            .start(LocalDate.of(2020, 12, 1))
            .end(LocalDate.of(2020, 12, 1))
            .build();

    public final Food dessert1 = Food.builder()
            .id(3L)
            .name("")
            .service(ServiceEnum.DESSERT)
            .start(LocalDate.of(2020, 12, 1))
            .end(LocalDate.of(2020, 12, 1))
            .build();

    public final Food dish2 = Food.builder()
            .id(4L)
            .name("")
            .service(ServiceEnum.DISH)
            .start(LocalDate.of(2020, 12, 2))
            .end(LocalDate.of(2020, 12, 2))
            .build();

    public final Order order1WithDay1Soup1Dish1Dessert1Client1 = Order.builder()
            .id(1L)
            .clientId(client1.getId())
            .delivery(true)
            .formula(Formula.MENU)
            .soupId(soup1.getId())
            .dishId(dish1.getId())
            .dessertId(dessert1.getId())
            .day(day1)
            .quantity(3)
            .build();

    public final Order order2WithDay1Dish1Client2 = Order.builder()
            .id(2L)
            .clientId(client2.getId())
            .delivery(true)
            .formula(Formula.DISH)
            .dishId(dish1.getId())
            .day(day1)
            .quantity(2)
            .build();

    public final Order order3WithDay2Dish2Client1 = Order.builder()
            .id(3L)
            .clientId(client1.getId())
            .delivery(true)
            .formula(Formula.DISH)
            .dishId(dish2.getId())
            .day(day2)
            .quantity(0)
            .build();

    public final Order order4WithDay1Soup1Dish1Dessert1Client1ZeroQuantity = Order.builder()
            .id(4L)
            .clientId(client1.getId())
            .delivery(true)
            .formula(Formula.DISH_DESSERT)
            .dishId(dish1.getId())
            .dessertId(dessert1.getId())
            .day(day1)
            .quantity(0)
            .build();
}
