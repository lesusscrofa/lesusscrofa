package be.susscrofa.api.service;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import be.susscrofa.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final static String MEAL_TYPE_INVALID = "Meal type %s doesn't match foods content";

	private final static String MEAL_DATE_INVALID = "Meal dates doesn't match food %s %s %s";

	private final static String ORDER_NOT_FOUND = "Order with id %d not found";

	private final static String ORDER_ALREADY_EXIST = "An order already exist for client %d, day %s and type %s";

	private final OrderRepository orderRepository;

	private final FoodService foodService;

	private final FormulaPriceService formulaPriceService;

	private final ClientService clientService;

	public List<Order> getOrder(long clientId, LocalDate date) {
		return this.orderRepository.findByClientIdAndDay(clientId, date);
	}

	public List<Order> getOrder(long clientId, LocalDate date, Formula formula) {
		return this.orderRepository.findByClientIdAndDayAndFormula(clientId, date, formula);
	}

	public Order create(@NotNull Order order) {
		if(!Formula.OTHER.equals(order.getFormula())
				&& orderRepository.existsByClientIdAndDayAndFormula(order.getClientId(),
																	order.getDay(),
																	order.getFormula())) {
			throw new EntityAlreadyExistException(
					String.format(	ORDER_ALREADY_EXIST,
									order.getClientId(),
									order.getDay(),
									order.getFormula()));
		}

		return this.save(order);
	}

	public Order update(@NotNull Order order) {
		if(!orderRepository.existsById(order.getId())) {
			throw  new EntityNotFoundException(String.format(ORDER_NOT_FOUND, order.getId()));
		}

		return this.save(order);
	}

	private Order save(@NotNull Order order) {
		var client = clientService.getClient(order.getClientId());

		order.isValid();

		if(!order.checkIfMealsMatchMealType()) {
			throw new IllegalArgumentException(
					String.format(MEAL_TYPE_INVALID, order.getFormula()));
		}

		if(!order.checkIfMealsDatesAreValid(this::getFoodFromRepository)) {
			throw new IllegalArgumentException(
					String.format(MEAL_DATE_INVALID,
							order.getSoupId(), order.getDishId(), order.getDessertId()));
		}

		if(Formula.OTHER == order.getFormula()) {
			var food = foodService.getById(order.getOtherId());
			//order.setPrice(food.getPrice());
		}
		else{
			var formula = formulaPriceService.getActive(order.getFormula());
			//order.setPrice(formula.getPrice());
		}

		return orderRepository.save(order);
	}

	public void delete(Long id) {
		if(!orderRepository.existsById(id)) {
			throw  new EntityNotFoundException(String.format(ORDER_NOT_FOUND, id));
		}

		orderRepository.deleteById(id);
	}

	private Food getFoodFromRepository(Long id) {
		return Optional.ofNullable(id)
				.map(foodService::getById)
				.orElse(null);
	}
}
