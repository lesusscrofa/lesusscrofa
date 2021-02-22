package be.susscrofa.api.view;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
public class DeliverySummaryView {

    private long id;

    private String firstName;

    private String lastName;

    private int deliveryPosition;

    private String deliveryStreet;

    private Long deliveryZoneId;

    private Integer deliveryZipCode;

    private String deliveryCity;

    private String deliveryPhone;

    private Long deliveryManId;

    private String deliveryManFirstName;

    private String deliveryManLastName;

    private LocalDate day;

    private String soupName;

    private Integer soupQuantity;

    private String dishName;

    private Integer dishQuantity;

    private String alternativeDishName;

    private Integer alternativeDishQuantity;

    private String dessertName;

    private Integer dessertQuantity;

    private List<OtherFoodDeliveryView> others;

    @Data
    @Builder
    public static class OtherFoodDeliveryView {

        private String name;

        private Integer quantity;
    }

    public static List<DeliverySummaryView> from(List<DeliveryView> foodsOrdersClient) {
        return foodsOrdersClient
                .stream()
                .flatMap(DeliverySummaryView::toDeliveries)
                .collect(Collectors.toList());
    }

    private static Stream<DeliverySummaryView> toDeliveries(DeliveryView deliveryView) {
       return deliveryView.getFoodsOrders()
                .stream()
                .collect(
                        HashMap<LocalDate, DeliverySummaryView>::new,
                        (acc, foodOrder) -> updateDeliveriesMap(acc, deliveryView, foodOrder),
                        HashMap::putAll
                )
                .values()
                .stream();
    }

    private static void updateDeliveriesMap(Map<LocalDate, DeliverySummaryView> deliveries, DeliveryView deliveryView, DeliveryView.FoodOrderedByClientView foodOrder) {
        var delivery = deliveries.get(foodOrder.getId().getOrderDay());

        if(delivery == null) {
            delivery = create(deliveryView, foodOrder);
            deliveries.put(foodOrder.getId().getOrderDay(), delivery);
        }

        update(delivery, foodOrder);
    }

    private static DeliverySummaryView create(DeliveryView client, DeliveryView.FoodOrderedByClientView foodOrder) {
        return DeliverySummaryView
                .builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .deliveryPhone(client.getDeliveryPhone())
                .deliveryStreet(client.getDeliveryStreet())
                .deliveryZoneId(client.getDeliveryZoneId())
                .deliveryZipCode(client.getDeliveryZipCode())
                .deliveryCity(client.getDeliveryCity())
                .deliveryManId(client.getDeliveryManId())
                .deliveryManFirstName(client.getDeliveryManFirstName())
                .deliveryManLastName(client.getDeliveryManLastName())
                .deliveryPosition(client.getDeliveryPosition())
                .day(foodOrder.getId().getOrderDay())
                .others(new ArrayList<>())
                .build();
    }

    private static DeliverySummaryView update(DeliverySummaryView delivery, DeliveryView.FoodOrderedByClientView foodOrder) {
        switch (foodOrder.getFoodService()) {
            case SOUP -> {
                delivery.setSoupName(foodOrder.getFoodName());
                delivery.setSoupQuantity(foodOrder.getQuantity());
            }
            case DISH -> {
                delivery.setDishName(foodOrder.getFoodName());
                delivery.setDishQuantity(foodOrder.getQuantity());
            }
            case ALTERNATIVE_DISH -> {
                delivery.setAlternativeDishName(foodOrder.getFoodName());
                delivery.setAlternativeDishQuantity(foodOrder.getQuantity());
            }
            case DESSERT -> {
                delivery.setDessertName(foodOrder.getFoodName());
                delivery.setDessertQuantity(foodOrder.getQuantity());
            }
            case OTHER -> delivery.getOthers().add(OtherFoodDeliveryView.builder()
                    .name(foodOrder.getFoodName())
                    .quantity(foodOrder.getQuantity())
                    .build());
            default -> throw new RuntimeException("Missing service " + foodOrder.getFoodService());
        }

        return delivery;
    }
}
