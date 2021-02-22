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

public class OrderedFoodResourceTest extends AbstractResourceTest {

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
}
