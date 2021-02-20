package be.susscrofa.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.model.Client;
import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@InjectMocks
	OrderService orderService;

	@Mock
	FormulaPriceService formulaPriceService;

	@Mock
    OrderRepository orderRepository;

	@Mock
	FoodService foodService;

	@Mock
	ClientService clientService;
	
	@Test
	void getOrders() {
		long clientId = 1L;
		var day = LocalDate.now();
		
		var orders = List.of(Order.builder()
				.build());

		when(orderRepository.findByClientIdAndDay(clientId, day))
			.thenReturn(orders);
		
		var response = orderService.getOrder(clientId, day);
		
		Assertions.assertThat(response).isEqualTo(orders);
	}

	@Test
	void create() {
		var price = BigDecimal.valueOf(11.3);

		var client = Client.builder()
				.id(1L)
				.build();

		var soup = Food.builder()
				.id(1L)
				.service(ServiceEnum.SOUP)
				.build();
		var dish = Food.builder()
				.id(2L)
				.service(ServiceEnum.DISH)
				.build();
		var dessert = Food.builder()
				.id(3L)
				.service(ServiceEnum.DESSERT)
				.build();

		var order =
				Order.builder()
						.clientId(client.getId())
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(1)
						.build();

		when(clientService.getClient(order.getClientId()))
				.thenReturn(client);

		when(orderRepository.existsByClientIdAndDayAndFormula(client.getId(), order.getDay(), order.getFormula()))
				.thenReturn(false);

		when(formulaPriceService.getActive(order.getFormula()))
				.thenReturn(FormulaPrice.builder().price(price).build());

		when(foodService.getById(soup.getId()))
				.thenReturn(soup);

		when(foodService.getById(dish.getId()))
				.thenReturn(dish);

		when(foodService.getById(dessert.getId()))
				.thenReturn(dessert);

		when(orderRepository.save(order))
				.thenReturn(order);

		var res = orderService.create(order);

		assertThat(order.getClientId()).isEqualTo(res.getClientId());
		assertThat(order.getDay()).isEqualTo(res.getDay());
		assertThat(order.getFormula()).isEqualTo(res.getFormula());
		assertThat(order.getSoupId()).isEqualTo(res.getSoupId());
		assertThat(order.getDishId()).isEqualTo(res.getDishId());
		assertThat(order.getDessertId()).isEqualTo(res.getDessertId());
		assertThat(order.getQuantity()).isEqualTo(res.getQuantity());
		//assertThat(order.getPrice()).isEqualTo(price);
	}

	@Test
	void createOrder_alreadyExistTypeDifferentThanOther() {
		var client = Client.builder()
				.id(1L)
				.build();

		var soup = Food.builder()
				.id(1L)
				.service(ServiceEnum.SOUP)
				.build();
		var dish = Food.builder()
				.id(2L)
				.service(ServiceEnum.DISH)
				.build();
		var dessert = Food.builder()
				.id(3L)
				.service(ServiceEnum.DESSERT)
				.build();

		var order =
				Order.builder()
						.clientId(client.getId())
						.day(LocalDate.now())
						.formula(Formula.MENU)
						.soupId(soup.getId())
						.dishId(dish.getId())
						.dessertId(dessert.getId())
						.quantity(1)
						.build();

		when(orderRepository.existsByClientIdAndDayAndFormula(client.getId(), order.getDay(), order.getFormula()))
				.thenReturn(true);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(EntityAlreadyExistException.class);
	}

	@Test
	void createOrder_alreadyExistTypeOther() {
		var price = BigDecimal.valueOf(11.3);

		var client = Client.builder()
				.id(1L)
				.build();

		var dish = Food.builder()
				.id(2L)
				.service(ServiceEnum.DISH)
				.price(price)
				.build();

		var order =
				Order.builder()
						.clientId(client.getId())
						.day(LocalDate.now())
						.formula(Formula.OTHER)
						.otherId(dish.getId())
						.quantity(1)
						.build();

		when(clientService.getClient(order.getClientId()))
				.thenReturn(client);

		when(foodService.getById(dish.getId()))
				.thenReturn(dish);

		when(orderRepository.save(order))
				.thenReturn(order);

		var res = orderService.create(order);

		assertThat(order.getClientId()).isEqualTo(res.getClientId());
		assertThat(order.getDay()).isEqualTo(res.getDay());
		assertThat(order.getFormula()).isEqualTo(res.getFormula());
		assertThat(order.getSoupId()).isEqualTo(res.getSoupId());
		assertThat(order.getDishId()).isEqualTo(res.getDishId());
		assertThat(order.getDessertId()).isEqualTo(res.getDessertId());
		assertThat(order.getQuantity()).isEqualTo(res.getQuantity());
		//assertThat(order.getPrice()).isEqualTo(price);
	}

	@Test
	void createOrder_quantityBellowZero() {
		var order =
				Order.builder()
						.formula(Formula.MENU)
						.clientId(1L)
						.day(LocalDate.now())
						.soupId(1L)
						.dishId(2L)
						.dessertId(3L)
						.quantity(0)
						.build();

		when(orderRepository.existsByClientIdAndDayAndFormula(order.getClientId(), order.getDay(), order.getFormula()))
				.thenReturn(false);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Can't create order with zero quantity");

		verify(orderRepository, never()).save(order);
	}

	@Test
	void createOrder_mealDontMatchType() {
		var client = Client.builder().id(1L).build();

		var order =
				Order.builder()
						.clientId(client.getId())
						.formula(Formula.MENU)
						.soupId(1L)
						.dishId(2L)
						.dessertId(null)
						.quantity(1)
						.build();

		when(clientService.getClient(order.getClientId()))
				.thenReturn(client);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Meal type MENU doesn't match foods content");

		verify(orderRepository, never()).save(order);
	}

	@Test
	void createOrder_dateDontMatchType() {
		var client = Client.builder().id(1L).build();

		var dish = Food.builder()
				.id(1L)
				.start(LocalDate.of(2020, 12, 1))
				.end(LocalDate.of(2020, 12, 1))
				.build();

		var order =
				Order.builder()
						.formula(Formula.DISH)
						.clientId(client.getId())
						.day(LocalDate.of(2020, 12, 2))
						.dishId(dish.getId())
						.quantity(1)
						.build();

		when(clientService.getClient(order.getClientId()))
				.thenReturn(client);

		when(foodService.getById(dish.getId()))
				.thenReturn(dish);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Meal dates doesn't match food");

		verify(orderRepository, never()).save(order);
	}

	@Test
	void update() {
		var price = BigDecimal.valueOf(11.3);

		var client = Client.builder()
				.id(1L)
				.build();

		var dish = Food.builder()
				.id(2L)
				.service(ServiceEnum.DISH)
				.build();

		when(foodService.getById(dish.getId()))
				.thenReturn(dish);

		var order =
				Order.builder()
						.day(LocalDate.now())
						.clientId(client.getId())
						.formula(Formula.DISH)
						.dishId(dish.getId())
						.quantity(1)
						.build();

		when(clientService.getClient(order.getClientId()))
				.thenReturn(client);

		when(orderRepository.existsById(order.getId()))
				.thenReturn(true);

		when(formulaPriceService.getActive(order.getFormula()))
				.thenReturn(FormulaPrice.builder().price(price).build());

		when(orderRepository.save(order))
				.thenReturn(order);

		var res = orderService.update(order);

		assertThat(order.getClientId()).isEqualTo(res.getClientId());
		assertThat(order.getDay()).isEqualTo(res.getDay());
		assertThat(order.getFormula()).isEqualTo(res.getFormula());
		assertThat(order.getSoupId()).isEqualTo(res.getSoupId());
		assertThat(order.getDishId()).isEqualTo(res.getDishId());
		assertThat(order.getDessertId()).isEqualTo(res.getDessertId());
		assertThat(order.getQuantity()).isEqualTo(res.getQuantity());
		//assertThat(order.getPrice()).isEqualTo(price);
	}

	@Test
	void update_DontExist() {
		var order =
				Order.builder()
						.id(1L)
						.formula(Formula.MENU)
						.soupId(1L)
						.dishId(2L)
						.dessertId(3L)
						.quantity(1)
						.build();

		when(orderRepository.existsById(order.getId()))
				.thenReturn(false);

		assertThatThrownBy(() -> orderService.update(order))
			.isInstanceOf(EntityNotFoundException.class);
	}
}
