package be.susscrofa.api.resource;

import be.susscrofa.api.model.FoodOrderData;
import be.susscrofa.api.repository.ClientRepository;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.repository.DeliveryZoneRepository;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FoodOrderResourceTest extends AbstractResourceTest {

    @Autowired
    private MockMvc mockMvc;

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
    void findFoodsOrders() throws Exception {
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

        var soup1 = data.soup1;
        var dish1 = data.dish1;
        var dessert1 = data.dessert1;
        var dish2 = data.dish2;

        soup1 = foodRepository.save(soup1);
        dish1 = foodRepository.save(dish1);
        dessert1 = foodRepository.save(dessert1);
        dish2 = foodRepository.save(dish2);

        var order1WithDay1Soup1Dish1Dessert1Client1 = data.order1WithDay1Soup1Dish1Dessert1Client1;
        order1WithDay1Soup1Dish1Dessert1Client1.setClientId(client1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setSoupId(soup1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDishId(dish1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDessertId(dessert1.getId());
        orderRepository.save(order1WithDay1Soup1Dish1Dessert1Client1);
        var order2WithDay1Dish1Client2 = data.order2WithDay1Dish1Client2;
        order2WithDay1Dish1Client2.setClientId(client2.getId());
        order2WithDay1Dish1Client2.setDishId(dish1.getId());
        orderRepository.save(order2WithDay1Dish1Client2);
        var order3WithDay2Dish2Client1 = data.order3WithDay2Dish2Client1;
        order3WithDay2Dish2Client1.setClientId(client1.getId());
        order3WithDay2Dish2Client1.setDishId(dish2.getId());
        orderRepository.save(order3WithDay2Dish2Client1);

        this.mockMvc.perform(get("/api/orders/{day}/foods", data.day1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].foodId", hasItem(soup1.getId().intValue())))
                .andExpect(jsonPath("$[*].foodId", hasItem(dish1.getId().intValue())))
                .andExpect(jsonPath("$[*].foodId", hasItem(dessert1.getId().intValue())))
        ;
    }

    @Test
    void findFoodsOrdersByClient() throws Exception {
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

        var soup1 = data.soup1;
        var dish1 = data.dish1;
        var dessert1 = data.dessert1;
        var dish2 = data.dish2;

        soup1 = foodRepository.save(soup1);
        dish1 = foodRepository.save(dish1);
        dessert1 = foodRepository.save(dessert1);
        dish2 = foodRepository.save(dish2);

        var order1WithDay1Soup1Dish1Dessert1Client1 = data.order1WithDay1Soup1Dish1Dessert1Client1;
        order1WithDay1Soup1Dish1Dessert1Client1.setClientId(client1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setSoupId(soup1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDishId(dish1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDessertId(dessert1.getId());
        orderRepository.save(data.order1WithDay1Soup1Dish1Dessert1Client1);
        var order2WithDay1Dish1Client2 = data.order2WithDay1Dish1Client2;
        order2WithDay1Dish1Client2.setClientId(client2.getId());
        order2WithDay1Dish1Client2.setDishId(dish1.getId());
        orderRepository.save(data.order2WithDay1Dish1Client2);
        var order3WithDay2Dish2Client1 = data.order3WithDay2Dish2Client1;
        order3WithDay2Dish2Client1.setClientId(client1.getId());
        order3WithDay2Dish2Client1.setDishId(dish2.getId());
        orderRepository.save(data.order3WithDay2Dish2Client1);

        this.mockMvc.perform(get("/api/orders/{day}/foods/byClient", data.day1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", hasItem(client1.getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(client2.getId().intValue())))
        ;
    }

    @Test
    void findFoodsOrdersByClientFlat() throws Exception {
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

        var order1WithDay1Soup1Dish1Dessert1Client1 = data.order1WithDay1Soup1Dish1Dessert1Client1;
        order1WithDay1Soup1Dish1Dessert1Client1.setClientId(client1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setSoupId(soup1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDishId(dish1.getId());
        order1WithDay1Soup1Dish1Dessert1Client1.setDessertId(dessert1.getId());
        orderRepository.save(data.order1WithDay1Soup1Dish1Dessert1Client1);
        var order2WithDay1Dish1Client2 = data.order2WithDay1Dish1Client2;
        order2WithDay1Dish1Client2.setDishId(dish1.getId());
        order2WithDay1Dish1Client2.setClientId(client2.getId());
        orderRepository.save(data.order2WithDay1Dish1Client2);
        var order3WithDay2Dish2Client1 = data.order3WithDay2Dish2Client1;
        order3WithDay2Dish2Client1.setDishId(dish2.getId());
        order3WithDay2Dish2Client1.setClientId(client1.getId());
        orderRepository.save(data.order3WithDay2Dish2Client1);

        this.mockMvc.perform(get("/api/orders/{day}/foods/byClient/flat", data.day1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", hasItem(client1.getFirstName())))
                .andExpect(jsonPath("$[*].lastName", hasItem(client1.getLastName())))
                .andExpect(jsonPath("$[*].deliveryStreet", hasItem(client1.getDeliveryStreet())))
                .andExpect(jsonPath("$[*].deliveryZipCode", hasItem(client1.getDeliveryZipCode())))
                .andExpect(jsonPath("$[*].deliveryCity", hasItem(client1.getDeliveryCity())))
                .andExpect(jsonPath("$[*].deliveryPhone", hasItem(client1.getDeliveryPhone())))
                .andExpect(jsonPath("$[*].day", hasItem(data.day1.toString())))
                .andExpect(jsonPath("$[*].firstName", hasItem(client2.getFirstName())))
                .andExpect(jsonPath("$[*].lastName", hasItem(client2.getLastName())))
                .andExpect(jsonPath("$[*].deliveryStreet", hasItem(client2.getDeliveryStreet())))
                .andExpect(jsonPath("$[*].deliveryPhone", hasItem(client2.getDeliveryPhone())))
                .andExpect(jsonPath("$[*].deliveryZipCode", hasItem(client2.getDeliveryZipCode())))
                .andExpect(jsonPath("$[*].deliveryCity", hasItem(client2.getDeliveryCity())))
                .andExpect(jsonPath("$[*].day", hasItem(data.day1.toString())))
                .andExpect(jsonPath("$[0].deliveryManId").value(deliveryMan.getId()))
                .andExpect(jsonPath("$[1].deliveryManId").value(deliveryMan.getId()))
                .andExpect(jsonPath("$[0].deliveryManFirstName").value(deliveryMan.getFirstName()))
                .andExpect(jsonPath("$[1].deliveryManFirstName").value(deliveryMan.getFirstName()))
                .andExpect(jsonPath("$[0].deliveryManLastName").value(deliveryMan.getLastName()))
                .andExpect(jsonPath("$[1].deliveryManLastName").value(deliveryMan.getLastName()))
                .andExpect(jsonPath("$[0].deliveryZoneId").value(deliveryZone.getId()))
                .andExpect(jsonPath("$[1].deliveryZoneId").value(deliveryZone.getId()))
        ;
    }
}
