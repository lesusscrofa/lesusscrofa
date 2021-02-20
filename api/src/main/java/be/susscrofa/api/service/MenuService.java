package be.susscrofa.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.MenuFactory;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.service.exception.EntityNotFoundException;

import be.susscrofa.api.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Service
public class MenuService {

	private static final String MENU_DONT_EXIST = "Menu for date %s not found";
	private static final String DISH_ID_DONT_EXIST = "Dish with id %s not found";

	private final FoodService foodService;

	public List<Menu> findAll(LocalDate start, LocalDate end, boolean includeAlternative) {
		if(start == null || end == null) {
			throw new IllegalArgumentException("start and end date mandatory");
		}
		else {
			return findMenus(start, end, includeAlternative);
		}
	}

	private List<Menu> findMenus(@NotNull LocalDate start, @NotNull LocalDate end, boolean includeAlternative) {
		var foods =
				foodService.findAll(start, end);

		if(includeAlternative) {
			return MenuFactory.createMenuFromFoodsWithAlternative(foods, start, end);
		}
		else {
			return MenuFactory.createMenuFromFoodsWithoutAlternative(foods, start, end);
		}
	}

	public Menu create(@NotNull @Valid Menu menu) {
		if(menu.getDish() == null && menu.getAlternativeDish() == null) {
			throw new IllegalArgumentException("There must be at least one dish or alternative dish for one menu");
		}

		menu.setSoup(
				Optional.ofNullable(menu.getSoup())
						.map(f -> setService(f, ServiceEnum.SOUP))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::create)
						.orElse(null)
		);

		menu.setDish(
				Optional.ofNullable(menu.getDish())
						.map(f -> setService(f, ServiceEnum.DISH))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::create)
						.orElse(null)
		);

		menu.setDessert(
				Optional.ofNullable(menu.getDessert())
						.map(f -> setService(f, ServiceEnum.DESSERT))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::create)
						.orElse(null)
		);

		return menu;
	}

	public Menu update(@NotNull @Valid Menu menu) {
		foodService.findByStartDateAndService(menu.getDay(), ServiceEnum.DISH)
				.orElseThrow(() -> new EntityNotFoundException(String.format(MENU_DONT_EXIST, menu.getDay())));

		menu.setSoup(
				Optional.ofNullable(menu.getSoup())
						.map(f -> setService(f, ServiceEnum.SOUP))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::update)
						.orElse(null)
		);

		menu.setDish(
				Optional.ofNullable(menu.getDish())
						.map(f -> setService(f, ServiceEnum.DISH))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::update)
						.orElse(null)
		);

		menu.setDessert(
				Optional.ofNullable(menu.getDessert())
						.map(f -> setService(f, ServiceEnum.DESSERT))
						.map(f -> setMenuDateToFood(f, menu.getDay()))
						.map(foodService::update)
						.orElse(null)
		);

		return menu;
	}

	private Food setMenuDateToFood(Food food, LocalDate day) {
		food.setStart(day);
		food.setEnd(day);

		return food;
	}

	private Food setService(Food food, ServiceEnum service) {
		food.setService(service);

		return food;
	}
}
