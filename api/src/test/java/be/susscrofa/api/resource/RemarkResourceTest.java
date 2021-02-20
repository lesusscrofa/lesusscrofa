package be.susscrofa.api.resource;

import be.susscrofa.api.model.Client;
import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.model.Remark;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.ClientRepository;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.repository.DeliveryZoneRepository;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.repository.OrderRepository;
import be.susscrofa.api.repository.RemarkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RemarkResourceTest extends AbstractResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DeliveryZoneRepository deliveryZoneRepository;

    @Autowired
    private DeliveryManRepository deliveryManRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findRemarks() throws Exception {
        var now = LocalDate.now();

        var client1 = createAndSaveClient("Bob", "Leponge");

        var client2 = createAndSaveClient("Gerard", "Menvuca");

        var remark1 = Remark
                .builder()
                .clientId(client1.getId())
                .day(now)
                .message("remark1")
                .build();
        remarkRepository.save(remark1);

        var remark2 = Remark
                .builder()
                .clientId(client2.getId())
                .day(now)
                .message("remark2")
                .build();
        remarkRepository.save(remark2);

        var remark3 = Remark
                .builder()
                .clientId(client2.getId())
                .day(now.plusDays(1))
                .message("remark3")
                .build();
        remarkRepository.save(remark3);

        var remark4 = Remark
                .builder()
                .clientId(client1.getId())
                .message("remark4")
                .build();
        remarkRepository.save(remark4);

        Food food = Food
                .builder()
                .name("Spaget")
                .service(ServiceEnum.DISH)
                .build();
        food = foodRepository.save(food);

        Order order = Order
                .builder()
                .clientId(client1.getId())
                .delivery(true)
                .day(now)
                .dishId(food.getId())
                .formula(Formula.DISH)
                .quantity(1)
                .build();
        orderRepository.save(order);

        this.mockMvc.perform(get("/api/orders/{day}/remarks/", now.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[*].dailyMessage", hasItem("remark1")))
                .andExpect(jsonPath("$[*].clientMessage", hasItem("remark4")));
    }

    @Test
    public void createRemark() throws Exception {
        var now = LocalDate.now();

        var client = createAndSaveClient("Bob", "Leponge");

        var remark = Remark
                .builder()
                .message("remark")
                .build();

        this.mockMvc.perform(put("/api/clients/{clientId}/remarks/{day}", client.getId(), now.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(remark)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("clientId").value(client.getId().intValue()))
                .andExpect(jsonPath("day").value(now.toString()))
                .andExpect(jsonPath("message").value(remark.getMessage()));
    }

    @Test
    public void updateRemark() throws Exception {
        var now = LocalDate.now();

        var client = createAndSaveClient("Bob", "Leponge");

        var remark = Remark
                .builder()
                .clientId(client.getId())
                .day(now)
                .message("remark")
                .build();
        remarkRepository.save(remark);

        remark.setMessage("remarkUpdated");

        this.mockMvc.perform(put("/api/clients/{clientId}/remarks/{day}", client.getId(), now.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(remark)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("clientId").value(client.getId().intValue()))
                .andExpect(jsonPath("day").value(now.toString()))
                .andExpect(jsonPath("message").value("remarkUpdated"));
    }

    private Client createAndSaveClient(String firstName, String lastName) {
        var deliveryMan = DeliveryMan.builder()
                .firstName("Super")
                .lastName("Livreur")
                .build();
        deliveryManRepository.save(deliveryMan);

        var deliveryZone = DeliveryZone
                .builder()
                .deliveryManId(deliveryMan.getId())
                .name("boucle1")
                .build();
        deliveryZoneRepository.save(deliveryZone);

        var client = Client
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .deliveryZoneId(deliveryZone.getId())
                .build();

        return clientRepository.save(client);
    }
}
