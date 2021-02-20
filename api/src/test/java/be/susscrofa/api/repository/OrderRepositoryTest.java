package be.susscrofa.api.repository;

import be.susscrofa.api.model.Client;
import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.model.ServiceEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

public class OrderRepositoryTest extends AbstractRepositoryTest {

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
	void findByClientIdAndDay() {
		var deliveryZone = createDeliveryZone();

		var client1 = Client.builder()
				.firstName("Axel")
				.lastName("Aire")
				.deliveryZoneId(deliveryZone.getId())
				.build();

		client1 = clientRepository.save(client1);
		
		var client2 = Client.builder()
				.id(2L)
				.firstName("Maxime")
				.lastName("Homme")
				.deliveryZoneId(deliveryZone.getId())
				.build();

		client2 = clientRepository.save(client2);
		
		var dish1 = Food.builder()
				.id(1L)
				.name("")
				.service(ServiceEnum.DISH)
				.start(LocalDate.of(2020, 12, 1))
				.end(LocalDate.of(2020, 12, 1))
				.build();

		dish1 = foodRepository.save(dish1);
		
		var dish2 = Food.builder()
				.id(2L)
				.name("")
				.service(ServiceEnum.DISH)
				.start(LocalDate.of(2020, 12, 2))
				.end(LocalDate.of(2020, 12, 2))
				.build();

		dish2 = foodRepository.save(dish2);
		
		var order1 = Order.builder()
				.id(1L)
				.clientId(client1.getId())
				.delivery(true)
				.formula(Formula.DISH)
				.dishId(dish1.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();
		
		var order2 = Order.builder()
				.id(2L)
				.clientId(client2.getId())
				.delivery(true)
				.formula(Formula.DISH)
				.dishId(dish1.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();
		
		var order3 = Order.builder()
				.id(3L)
				.clientId(client1.getId())
				.delivery(true)
				.formula(Formula.DISH)
				.dishId(dish2.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();

		order1 = orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		
		var orderItems = orderRepository.findByClientIdAndDay(client1.getId(), order1.getDay());
		
		Assertions.assertThat(orderItems.get(0)).isEqualTo(order1);
	}

	@Test
	void existsByClientIdAndDayAndType() {
		var deliveryZone = createDeliveryZone();


		var client = Client.builder()
				.firstName("Axel")
				.lastName("Aire")
				.deliveryZoneId(deliveryZone.getId())
				.build();

		client = clientRepository.save(client);

		var dish1 = Food.builder()
				.id(1L)
				.name("")
				.service(ServiceEnum.DISH)
				.start(LocalDate.of(2020, 12, 1))
				.end(LocalDate.of(2020, 12, 1))
				.build();

		dish1 = foodRepository.save(dish1);

		var dish2 = Food.builder()
				.id(2L)
				.name("")
				.service(ServiceEnum.DISH)
				.start(LocalDate.of(2020, 12, 2))
				.end(LocalDate.of(2020, 12, 2))
				.build();

		dish2 = foodRepository.save(dish2);

		var order1 = Order.builder()
				.id(1L)
				.clientId(client.getId())
				.delivery(true)
				.formula(Formula.DISH)
				.dishId(dish1.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();

		var order2 = Order.builder()
				.id(2L)
				.clientId(client.getId())
				.delivery(true)
				.formula(Formula.ALTERNATIVE_DISH)
				.dishId(dish1.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();

		var order3 = Order.builder()
				.id(3L)
				.clientId(client.getId())
				.delivery(true)
				.formula(Formula.DISH)
				.dishId(dish2.getId())
				.day(LocalDate.now())
				.quantity(0)
				.build();


		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);

		var result = orderRepository.existsByClientIdAndDayAndFormula(client.getId(), order1.getDay(), Formula.DISH);

		assertThat(result).isEqualTo(true);
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
}
