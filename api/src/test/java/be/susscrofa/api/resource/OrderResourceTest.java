package be.susscrofa.api.resource;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import be.susscrofa.api.model.Client;
import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.ClientRepository;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.repository.DeliveryZoneRepository;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.repository.FormulaPriceRepository;
import be.susscrofa.api.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class OrderResourceTest extends AbstractResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;

	@Autowired
	private DeliveryManRepository deliveryManRepository;

	@Autowired
	private FormulaPriceRepository formulaPriceRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void findClientOrder() throws Exception {
		var firstName = "Sandra";
		var lastName = "Nicouverture";

		var client = Client.builder()
				.firstName(firstName)
				.lastName(lastName)
				.deliveryZoneId(createDeliveryZone().getId())
				.build();
		
		client = clientRepository.save(client);
		
		var day = LocalDate.now();
		var soup = Food.builder()
				.name("Cresson")
				.start(day)
				.end(day)
				.service(ServiceEnum.SOUP)
				.build();
		var dish = Food.builder()
				.name("Boulette sauce tomate")
				.start(day)
				.end(day)
				.service(ServiceEnum.DISH)
				.build();
		var dessert = Food.builder()
				.name("Mousse au chocolat")
				.start(day)
				.end(day)
				.service(ServiceEnum.DESSERT)
				.build();

		foodRepository.save(soup);
		foodRepository.save(dish);
		foodRepository.save(dessert);
		
		var quantity = 5;

		var order = Order.builder()
				.clientId(client.getId())
				.delivery(true)
				.formula(Formula.MENU)
				.soupId(soup.getId())
				.dishId(dish.getId())
				.dessertId(dessert.getId())
				.quantity(quantity)
				.day(day)
				.build();
		
		orderRepository.save(order);
		
		this.mockMvc.perform(get("/api/clients/{clientId}/orders", client.getId())
				.param("day", day.toString()))
			.andDo(print())
			.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].clientId").value(client.getId()))
				.andExpect(jsonPath("$[0].soupId").value(soup.getId()))
				.andExpect(jsonPath("$[0].dishId").value(dish.getId()))
				.andExpect(jsonPath("$[0].dessertId").value(dessert.getId()))
				.andExpect(jsonPath("$[0].quantity").value(quantity))
				.andExpect(jsonPath("$[0].day").value(day.toString()));
	}

	@Test
	void create() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var dessert = Food.builder()
				.name("dessert")
				.service(ServiceEnum.DESSERT)
				.build();
		dessert = foodRepository.save(dessert);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(1)
						.build();

		this.mockMvc.perform(post("/api/orders")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(order)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("day").value(order.getDay().toString()))
			.andExpect(jsonPath("formula").value(order.getFormula().toString()))
			.andExpect(jsonPath("soupId").value(order.getSoupId()))
			.andExpect(jsonPath("dishId").value(order.getDishId()))
			.andExpect(jsonPath("dessertId").value(order.getDessertId()))
			.andExpect(jsonPath("quantity").value(order.getQuantity()));
	}

	@Test
	void createForOtherFood() throws Exception {
		var client = createClient();

		var vegetable = Food.builder()
				.name("vegetable")
				.service(ServiceEnum.OTHER)
				.price(BigDecimal.ONE)
				.build();
		vegetable = foodRepository.save(vegetable);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.now())
						.formula(Formula.OTHER)
						.otherId(vegetable.getId())
						.quantity(1)
						.build();

		this.mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("day").value(order.getDay().toString()))
				.andExpect(jsonPath("delivery").value(order.getDelivery()))
				.andExpect(jsonPath("formula").value(order.getFormula().toString()))
				.andExpect(jsonPath("soupId").value(order.getSoupId()))
				.andExpect(jsonPath("dishId").value(order.getDishId()))
				.andExpect(jsonPath("dessertId").value(order.getDessertId()))
				.andExpect(jsonPath("otherId").value(order.getOtherId()))
				.andExpect(jsonPath("quantity").value(order.getQuantity()));
	}

	@Test
	@Transactional
	@Rollback
	void create_priceDontExist() throws Exception {
		formulaPriceRepository.deleteAll();

		var client = createClient();

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(false)
						.day(LocalDate.of(2020, 10, 1))
						.formula(Formula.DISH)
						.dishId(dish.getId())
						.quantity(1)
						.build();

		this.mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isConflict());
	}

	@Test
	void create_quantityBellowZero() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var dessert = Food.builder()
				.name("dessert")
				.service(ServiceEnum.DESSERT)
				.build();
		dessert = foodRepository.save(dessert);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(0)
						.build();

		this.mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorMessage").value("Can't create order with zero quantity"));
	}

	@Test
	void create_missingClientPriceQuantity() throws Exception {
		var order =
				Order.builder()
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.build();

		this.mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("client must be not null")))
				.andExpect(jsonPath("$.errors", hasItem("quantity must be not null")));
	}

	@Test
	void create_orderAlreadyExist() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var dessert = Food.builder()
				.name("dessert")
				.service(ServiceEnum.DESSERT)
				.build();
		dessert = foodRepository.save(dessert);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.of(2020, 12, 8))
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(1)
						.build();

		order = orderRepository.save(order);

		this.mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("errorMessage").value(String.format("An order already exist for client %d, day 2020-12-08 and type MENU", client.getId())));
	}

	@Test
	void update() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var dessert = Food.builder()
				.name("dessert")
				.service(ServiceEnum.DESSERT)
				.build();
		dessert = foodRepository.save(dessert);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(1)
						.build();

		order = orderRepository.save(order);

		var updatedOrder = Order.builder()
				.clientId(client.getId())
				.delivery(false)
				.day(LocalDate.now())
				.formula(Formula.MENU)
				.soupId(soup.getId())
				.dishId(dish.getId())
				.dessertId(dessert.getId())
				.quantity(3)
				.build();

		this.mockMvc.perform(put("/api/orders/{id}", order.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedOrder)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("day").value(updatedOrder.getDay().toString()))
				.andExpect(jsonPath("delivery").value(updatedOrder.getDelivery()))
				.andExpect(jsonPath("formula").value(updatedOrder.getFormula().toString()))
				.andExpect(jsonPath("soupId").value(updatedOrder.getSoupId()))
				.andExpect(jsonPath("dishId").value(updatedOrder.getDishId()))
				.andExpect(jsonPath("dessertId").value(updatedOrder.getDessertId()))
				.andExpect(jsonPath("quantity").value(updatedOrder.getQuantity()));
	}

	@Test
	void update_orderNotFound() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var dish = Food.builder()
				.name("dish")
				.service(ServiceEnum.DISH)
				.build();
		dish = foodRepository.save(dish);

		var dessert = Food.builder()
				.name("dessert")
				.service(ServiceEnum.DESSERT)
				.build();
		dessert = foodRepository.save(dessert);

		var order = Order.builder()
				.clientId(client.getId())
				.delivery(true)
				.day(LocalDate.now())
				.formula(Formula.MENU)
				.soupId(soup.getId())
				.dishId(dish.getId())
				.dessertId(dessert.getId())
				.quantity(3)
				.build();

		this.mockMvc.perform(put("/api/orders/{id}", 3)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("errorMessage").value("Order with id 3 not found"));
	}

	@Test
	void update_menuDontMatch() throws Exception {
		var client = createClient();

		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		soup = foodRepository.save(soup);

		var order =
				Order.builder()
						.clientId(client.getId())
						.delivery(true)
						.day(LocalDate.now())
						.formula(Formula.SOUP)
						.soupId(soup.getId())
						.quantity(1)
						.build();

		order = orderRepository.save(order);

		var updatedOrder = Order.builder()
				.clientId(client.getId())
				.delivery(true)
				.day(LocalDate.now())
				.formula(Formula.MENU)
				.soupId(soup.getId())
				.quantity(3)
				.build();

		this.mockMvc.perform(put("/api/orders/{id}", order.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedOrder)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("errorMessage").value("Meal type MENU doesn't match foods content"));
	}

	private DeliveryZone createDeliveryZone() {
		var deliveryMan = this.deliveryManRepository.save(DeliveryMan
				.builder()
				.firstName("first")
				.lastName("last")
				.build());

		return this.deliveryZoneRepository.save(DeliveryZone
				.builder()
				.deliveryManId(deliveryMan.getId())
				.name("zone1")
				.build());
	}

	private Client createClient() {
		var deliveryZone = createDeliveryZone();

		return clientRepository.save(Client.builder()
				.firstName("f")
				.lastName("l")
				.deliveryZoneId(deliveryZone.getId())
				.reduction(10)
				.build());
	}
}
