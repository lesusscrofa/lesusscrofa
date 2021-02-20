package be.susscrofa.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.MenuModel;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.model.Menu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	MenuService menuService;

	@Mock
	FoodService foodService;

	@Captor
	ArgumentCaptor<Food> foodArgumentCaptor;

	@Test
	void findByDateWithAlternative() {
		LocalDate start = LocalDate.of(2020,12,7);
		LocalDate end = LocalDate.of(2020,12,19);

		var model = new MenuModel();

		when(foodService.findAll(start, end))
				.thenReturn(model.foodsForTwoWeeksBetween6And2712);

		var response = menuService.findAll(start, end, true);

		Assertions.assertThat(response).containsExactlyInAnyOrderElementsOf(model.expectedMenusForTwoWeeksBetween7And1912);
	}

	@Test
	void findByDateWithoutAlternative() {
		LocalDate start = LocalDate.of(2020,12,7);
		LocalDate end = LocalDate.of(2020,12,19);

		var model = new MenuModel();

		when(foodService.findAll(start, end))
				.thenReturn(model.foodsForTwoWeeksBetween6And2712);

		var response = menuService.findAll(start, end, false);

		Assertions.assertThat(response).isEqualTo(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative);
	}

	@Test
	void findByDate_badParameters() {
		List<Menu> menus = List.of();
		LocalDate start = LocalDate.now();
		LocalDate end = LocalDate.now();

		assertThatThrownBy(() -> menuService.findAll(start, null, true))
			.isInstanceOf(IllegalArgumentException.class);

		assertThatThrownBy(() -> menuService.findAll(null, end, true))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void create() {
		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();
		var dish = Food.builder().
				name("dish")
				.service(ServiceEnum.DISH)
				.build();

		var menu = Menu.builder()
				.soup(soup)
				.dish(dish)
				.build();

		when(foodService.create(soup)).thenReturn(soup);
		when(foodService.create(dish)).thenReturn(dish);

		var result = menuService.create(menu);

		verify(foodService, times(2)).create(foodArgumentCaptor.capture());

		Assertions.assertThat(result).isEqualTo(menu);

		assertThat(foodArgumentCaptor.getAllValues().get(0))
				.isEqualTo(menu.getSoup());
		assertThat(foodArgumentCaptor.getAllValues().get(1))
				.isEqualTo(menu.getDish());
	}

	@Test
	void create_missingDish() {
		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.build();

		var menu = Menu.builder()
				.soup(soup)
				.build();

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);

		verify(foodService, never()).create(foodArgumentCaptor.capture());
	}

	@Test
	void update() {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder()
				.name("soup")
				.service(ServiceEnum.SOUP)
				.start(day)
				.end(day)
				.build();
		var dish = Food.builder()
				.id(1L)
				.service(ServiceEnum.DISH)
				.start(day)
				.end(day)
				.build();
		var dessert = Food.builder()
				.service(ServiceEnum.DESSERT)
				.start(day)
				.end(day)
				.name("dessert").build();

		var menu = Menu.builder()
				.day(day)
				.soup(soup)
				.dish(dish)
				.dessert(dessert)
				.build();

		when(foodService.update(soup)).thenReturn(soup);
		when(foodService.update(dish)).thenReturn(dish);
		when(foodService.update(dessert)).thenReturn(dessert);

		when(foodService.findByStartDateAndService(day, ServiceEnum.DISH))
				.thenReturn(Optional.of(dish));

		var result = menuService.update(menu);

		Assertions.assertThat(result).isEqualTo(menu);
	}

	@Test
	void update_menuDontExist() {
		var dish = Food.builder()
				.id(1L)
				.start(LocalDate.of(2020, 12, 1))
				.end(LocalDate.of(2020, 12, 1))
				.build();
		var day = LocalDate.of(2020, 12, 1);

		var menu = Menu.builder()
				.day(day)
				.soup(Food.builder().build())
				.dish(dish)
				.dessert(Food.builder().build())
				.build();

		when(foodService.findByStartDateAndService(day, ServiceEnum.DISH))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuService.update(menu))
				.isInstanceOf(EntityNotFoundException.class);

		verify(foodService, never()).update(foodArgumentCaptor.capture());
	}
}
