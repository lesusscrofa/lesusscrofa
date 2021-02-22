package be.susscrofa.api.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyOrderRemarkView implements Serializable {

    @Id
    private Long clientId;

    private String clientFirstName;

    private String clientLastName;

    private LocalDate day;

    private String clientMessage;

    private String dailyMessage;

    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "clientId", referencedColumnName = "clientId"),
        @JoinColumn(name = "orderDay", referencedColumnName = "day")
    })
    private List<DeliveryView.FoodOrderedByClientView> foodsOrders;
}
