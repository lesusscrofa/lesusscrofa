package be.susscrofa.api.resource;

import java.time.LocalDate;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.MenuModel;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.model.Menu;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MenuResourceTest extends AbstractResourceTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void findMenusByDay() throws Exception {
		LocalDate startDate = LocalDate.of(2020, 12, 7);
		LocalDate endDate = LocalDate.of(2020, 12, 19);

		var model = new MenuModel();

		foodRepository.saveAll(model.foodsForTwoWeeksBetween6And2712);


		this.mockMvc.perform(get("/api/menus?start={start}&end={end}", startDate, endDate))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[*].day",
					hasItem(model.expectedMenusForTwoWeeksBetween7And1912
						.get(0).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
					hasItem(model.expectedMenusForTwoWeeksBetween7And1912
						.get(1).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
					hasItem(model.expectedMenusForTwoWeeksBetween7And1912
						.get(2).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
					hasItem(model.expectedMenusForTwoWeeksBetween7And1912
						.get(3).getDay().toString())))
				;
	}

	@Test
	public void findMenusByDay_withoutAlternative() throws Exception {
		LocalDate startDate = LocalDate.of(2020, 12, 7);
		LocalDate endDate = LocalDate.of(2020, 12, 19);

		var model = new MenuModel();

		foodRepository.saveAll(model.foodsForTwoWeeksBetween6And2712);


		this.mockMvc.perform(get("/api/menus?start={start}&end={end}&includeAlternative=false", startDate, endDate))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[*].day",
						hasItem(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative
								.get(0).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
						hasItem(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative
								.get(1).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
						hasItem(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative
								.get(2).getDay().toString())))
				.andExpect(jsonPath("$[*].day",
						hasItem(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative
								.get(3).getDay().toString())));

	}

	@Test
	public void create() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder()
				.name("Cresson")
				.build();
		var dish = Food.builder()
				.name("Boulette sauce tomate")
				.build();
		var alternativeDish = Food.builder()
				.name("Pates au beurre")
				.build();
		var dessert = Food.builder()
				.name("Mousse au chocolat")
				.build();

		var menu = Menu.builder()
				.day(day)
				.soup(soup)
				.dish(dish)
				.dessert(dessert)
				.build();

		this.mockMvc.perform(post("/api/menus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menu)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("day").value(day.toString()))
				.andExpect(jsonPath("soup.name").value(soup.getName()))
				.andExpect(jsonPath("soup.service").value("SOUP"))
				.andExpect(jsonPath("dish.name").value(dish.getName()))
				.andExpect(jsonPath("dish.service").value("DISH"))
				.andExpect(jsonPath("dessert.name").value(dessert.getName()))
				.andExpect(jsonPath("dessert.service").value("DESSERT"));
	}

	@Test
	public void create_wrongService() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder()
				.name("Cresson")
				.service(ServiceEnum.DESSERT)
				.build();
		var dish = Food.builder()
				.name("Boulette sauce tomate")
				.build();
		var dessert = Food.builder()
				.name("Mousse au chocolat")
				.build();

		var menu = Menu.builder()
				.day(day)
				.soup(soup)
				.dish(dish)
				.dessert(dessert)
				.build();

		this.mockMvc.perform(post("/api/menus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menu)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("day").value(day.toString()))
				.andExpect(jsonPath("soup.name").value(soup.getName()))
				.andExpect(jsonPath("soup.service").value("SOUP"))
				.andExpect(jsonPath("dish.name").value(dish.getName()))
				.andExpect(jsonPath("dish.service").value("DISH"))
				.andExpect(jsonPath("dessert.name").value(dessert.getName()))
				.andExpect(jsonPath("dessert.service").value("DESSERT"));
	}

	@Test
	public void create_missingDish() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder().name("Cresson").build();
		var dessert = Food.builder().name("Mousse au chocolat").build();

		var menu = Menu.builder()
				.day(day)
				.soup(soup)
				.dessert(dessert)
				.build();

		this.mockMvc.perform(post("/api/menus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menu)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("errorMessage").value("There must be at least one dish or alternative dish for one menu"));
	}

	@Test
	public void update() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder()
				.service(ServiceEnum.SOUP)
				.start(day)
				.end(day)
				.name("Cresson")
				.build();
		var dish = Food.builder()
				.service(ServiceEnum.DISH)
				.start(day)
				.end(day)
				.name("Boulette sauce tomate")
				.build();
		var dessert = Food.builder()
				.service(ServiceEnum.DESSERT)
				.start(day)
				.end(day)
				.name("Mousse au chocolat")
				.build();

		var menu = Menu.builder()
				.day(day)
				.build();

		soup = foodRepository.save(soup);
		dish = foodRepository.save(dish);
		dessert = foodRepository.save(dessert);

		var menuDto = Menu.builder()
				.day(day)
				.soup(Food.builder()
						.id(soup.getId())
						.name(soup.getName()+"changed")
						.build())
				.dish(Food.builder()
						.id(dish.getId())
						.name(dish.getName()+"changed")
						.build())
				.dessert(Food.builder()
						.id(dessert.getId())
						.name(dessert.getName()+"changed")
						.build())
				.build();

		this.mockMvc.perform(put("/api/menus/{day}", menu.getDay())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("soup.id").value(soup.getId()))
				.andExpect(jsonPath("soup.name").value(soup.getName()+"changed"))
				.andExpect(jsonPath("soup.service").value("SOUP"))
				.andExpect(jsonPath("dish.id").value(dish.getId()))
				.andExpect(jsonPath("dish.name").value(dish.getName()+"changed"))
				.andExpect(jsonPath("dish.service").value("DISH"))
				.andExpect(jsonPath("dessert.id").value(dessert.getId()))
				.andExpect(jsonPath("dessert.name").value(dessert.getName()+"changed"))
				.andExpect(jsonPath("dessert.service").value("DESSERT"));
	}

	@Test
	public void update_wrongServiceSet() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder()
				.service(ServiceEnum.DESSERT)
				.start(day)
				.end(day)
				.name("Cresson")
				.build();
		var dish = Food.builder()
				.service(ServiceEnum.DISH)
				.start(day)
				.end(day)
				.name("Boulette sauce tomate")
				.build();
		var dessert = Food.builder()
				.service(ServiceEnum.DESSERT)
				.start(day)
				.end(day)
				.name("Mousse au chocolat")
				.build();

		var menu = Menu.builder()
				.day(day)
				.build();

		soup = foodRepository.save(soup);
		dish = foodRepository.save(dish);
		dessert = foodRepository.save(dessert);

		var menuDto = Menu.builder()
				.day(day)
				.soup(Food.builder()
						.id(soup.getId())
						.name(soup.getName()+"changed")
						.build())
				.dish(Food.builder()
						.id(dish.getId())
						.name(dish.getName()+"changed")
						.build())
				.dessert(Food.builder()
						.id(dessert.getId())
						.name(dessert.getName()+"changed")
						.build())
				.build();

		this.mockMvc.perform(put("/api/menus/{day}", menu.getDay())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("soup.id").value(soup.getId()))
				.andExpect(jsonPath("soup.name").value(soup.getName()+"changed"))
				.andExpect(jsonPath("soup.service").value("SOUP"))
				.andExpect(jsonPath("dish.id").value(dish.getId()))
				.andExpect(jsonPath("dish.name").value(dish.getName()+"changed"))
				.andExpect(jsonPath("dish.service").value("DISH"))
				.andExpect(jsonPath("dessert.id").value(dessert.getId()))
				.andExpect(jsonPath("dessert.name").value(dessert.getName()+"changed"))
				.andExpect(jsonPath("dessert.service").value("DESSERT"));
	}

	@Test
	public void update_menuDontExist() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var soup = Food.builder().name("Cressonchanged").build();
		var dish = Food.builder().name("Boulette sauce tomatechanged").build();
		var dessert = Food.builder().name("Mousse au chocolatchanged").build();

		var menuDto = Menu.builder()
				.day(day)
				.soup(soup)
				.dish(dish)
				.dessert(dessert)
				.build();

		this.mockMvc.perform(put("/api/menus/{day}", day)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDto)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void update_menuDayDontMatch() throws Exception {
		var day = LocalDate.of(2020, 12, 1);
		var updatedDay = LocalDate.of(2020, 12, 2);

		var soup = Food.builder()
				.name("Cresson")
				.service(ServiceEnum.SOUP)
				.start(day)
				.end(day)
				.build();
		soup = foodRepository.save(soup);
		var dish = Food.builder()
				.name("Boulette sauce tomate")
				.service(ServiceEnum.DISH)
				.start(day)
				.end(day)
				.build();
		dish = foodRepository.save(dish);
		var dessert = Food.builder()
				.name("Mousse au chocolat")
				.service(ServiceEnum.DESSERT)
				.start(day)
				.end(day)
				.build();
		dessert = foodRepository.save(dessert);

		var menuDto = Menu.builder()
				.day(updatedDay)
				.soup(Food.builder().name(soup.getName()).build())
				.dish(Food.builder().name(dish.getName()).build())
				.dessert(Food.builder().name(dessert.getName()).build())
				.build();

		this.mockMvc.perform(put("/api/menus/{day}", updatedDay)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDto)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
}
