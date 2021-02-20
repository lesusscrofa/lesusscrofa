package be.susscrofa.api.view;

import be.susscrofa.api.model.ServiceEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_with_delivery_detail_view")
public class ClientWithFoodOrderView {

    @Id
    private long id;

    private String firstName;

    private String lastName;

    private int deliveryPosition;

    private String deliveryStreet;

    private Long deliveryZoneId;

    private String deliveryZoneName;

    private Integer deliveryZipCode;

    private String deliveryCity;

    private String deliveryPhone;

    private Long deliveryManId;

    private String deliveryManFirstName;

    private String deliveryManLastName;

    @OneToMany
    @JoinColumn(name = "clientId")
    private List<FoodOrderGroupedByClient> foodsOrders;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "food_order_client_view")
    public static class FoodOrderGroupedByClient {

        @Value
        @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
        @AllArgsConstructor
        @Embeddable
        public static class Id implements Serializable {
            @NotNull
            Long clientId;

            @NotNull
            private LocalDate orderDay;

            @NotNull
            Long foodId;
        }

        @EmbeddedId
        @JsonUnwrapped
        private FoodOrderGroupedByClient.Id id;

        @NotNull
        private String foodName;

        @NotNull
        @Enumerated(EnumType.STRING)
        private ServiceEnum foodService;

        @NotNull
        private int quantity;
    }
}
