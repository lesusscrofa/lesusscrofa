package be.susscrofa.api.resource;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.FoodRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FoodResourceTest extends AbstractResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findByStartEndService() throws Exception {
        List<Food> goodFoods = List.of(
                Food.builder()
                        .name("1")
                        .service(ServiceEnum.ALTERNATIVE_DISH)
                        .start(LocalDate.of(2020, 12,7))
                        .end(LocalDate.of(2020, 12,11))
                        .price(BigDecimal.ONE)
                        .build(),
                Food.builder()
                        .name("2")
                        .service(ServiceEnum.ALTERNATIVE_DISH)
                        .start(LocalDate.of(2020, 12,14))
                        .end(LocalDate.of(2020, 12,18))
                        .price(BigDecimal.valueOf(2))
                        .build()
        );

        foodRepository.saveAll(goodFoods);

        var otherFood1 = Food.builder()
                .name("otherFood1")
                .service(ServiceEnum.SOUP)
                .start(LocalDate.of(2020, 12,11))
                .end(LocalDate.of(2020, 12,11))
                .price(BigDecimal.valueOf(1))
                .build();

        foodRepository.save(otherFood1);

        var otherFood2 = Food.builder()
                .name("otherFood2")
                .service(ServiceEnum.ALTERNATIVE_DISH)
                .start(LocalDate.of(2020, 12,7))
                .end(LocalDate.of(2020, 12,7))
                .price(BigDecimal.valueOf(1))
                .build();

        foodRepository.save(otherFood2);

        this.mockMvc.perform(
                get("/api/foods?start={start}&end={end}&service={service}",
                        "2020-12-08", "2020-12-14", ServiceEnum.ALTERNATIVE_DISH.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", hasItem(goodFoods.get(0).getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(goodFoods.get(1).getId().intValue())));
    }

    @Test
    void findBy_startDateIsMissing() throws Exception {
        this.mockMvc.perform(
                get("/api/foods?end={end}&service={service}",
                        "2020-12-14", ServiceEnum.ALTERNATIVE_DISH.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").value("if start date or end date is present, both parameters must be provided"));
    }

    @Test
    void findBy_serviceIsMissing() throws Exception {
        this.mockMvc.perform(
                get("/api/foods?start={start}&end={end}",
                        "2020-12-14", "2020-12-14")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByService() throws Exception {
        List<Food> goodFoods = List.of(
                Food.builder()
                        .name("1")
                        .service(ServiceEnum.ALTERNATIVE_DISH)
                        .start(LocalDate.of(2020, 12,7))
                        .end(LocalDate.of(2020, 12,11))
                        .price(BigDecimal.valueOf(1))
                        .build(),
                Food.builder()
                        .name("2")
                        .service(ServiceEnum.ALTERNATIVE_DISH)
                        .start(LocalDate.of(2020, 12,14))
                        .end(LocalDate.of(2020, 12,18))
                        .price(BigDecimal.valueOf(2))
                        .build(),
                Food.builder()
                        .name("otherFood2")
                        .service(ServiceEnum.ALTERNATIVE_DISH)
                        .start(LocalDate.of(2020, 12,7))
                        .end(LocalDate.of(2020, 12,7))
                        .price(BigDecimal.valueOf(1))
                        .build()
        );

        foodRepository.saveAll(goodFoods);

        var otherFood1 = Food.builder()
                .name("otherFood1")
                .service(ServiceEnum.SOUP)
                .start(LocalDate.of(2020, 12,11))
                .end(LocalDate.of(2020, 12,11))
                .price(BigDecimal.valueOf(1))
                .build();

        foodRepository.save(otherFood1);


        this.mockMvc.perform(
                get("/api/foods?service={service}",
                        ServiceEnum.ALTERNATIVE_DISH.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", hasItem(goodFoods.get(0).getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(goodFoods.get(1).getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(goodFoods.get(2).getId().intValue())));
    }

    @Test
    void create() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        this.mockMvc.perform(post("/api/foods")
            .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("service").value(service.toString()))
                .andExpect(jsonPath("start").value(start.toString()))
                .andExpect(jsonPath("end").value(end.toString()))
                .andExpect(jsonPath("price").value(price));
    }

    @Test
    void create_otherSameDay() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.OTHER;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        foodRepository.save(food);

        this.mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("service").value(service.toString()))
                .andExpect(jsonPath("start").value(start.toString()))
                .andExpect(jsonPath("end").value(end.toString()))
                .andExpect(jsonPath("price").value(price));
    }

    @Test
    void create_foodPartOfMenuAlreadyExistForSameDate() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        foodRepository.save(food);

        this.mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void update() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        food = foodRepository.save(food);

        var updatedFood = Food.builder()
                .name(name+"changed")
                .start(start.plusDays(1))
                .end(end.plusDays(1))
                .service(ServiceEnum.DESSERT)
                .price(price.add(BigDecimal.valueOf(10)))
                .build();

        this.mockMvc.perform(put("/api/foods/{id}", food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFood)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("name").value(name+"changed"))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("service").value(ServiceEnum.DESSERT.toString()))
                .andExpect(jsonPath("start").value(start.plusDays(1).toString()))
                .andExpect(jsonPath("end").value(end.plusDays(1).toString()))
                .andExpect(jsonPath("price").value(BigDecimal.valueOf(20)));
    }

    @Test
    void update_dontExist() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name+"changed")
                .start(start.plusDays(1))
                .end(end.plusDays(1))
                .service(ServiceEnum.DESSERT)
                .price(price.add(BigDecimal.valueOf(10)))
                .build();

        this.mockMvc.perform(put("/api/foods/{id}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_foodPartOfMenuAlreadyExistForSameDate() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        food = foodRepository.save(food);

        var duplicateFood = Food.builder()
                .name(name)
                .start(start.plusDays(1))
                .end(end.plusDays(1))
                .service(service)
                .price(price)
                .build();

        duplicateFood = foodRepository.save(duplicateFood);

        var updatedFood = Food.builder()
                .name(name+"changed")
                .start(duplicateFood.getStart())
                .end(duplicateFood.getEnd())
                .service(service)
                .price(price)
                .build();

        this.mockMvc.perform(put("/api/foods/{id}", food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFood)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void update_alternativeFoodAlreadyExistForSameDate() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.of(2020, 12, 2);
        var end = LocalDate.of(2020, 12, 6);
        var service = ServiceEnum.ALTERNATIVE_DISH;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        food = foodRepository.save(food);

        var duplicateFood = Food.builder()
                .name(name)
                .start(LocalDate.of(2020, 12, 8))
                .end(LocalDate.of(2020, 12, 10))
                .service(service)
                .price(price)
                .build();

        duplicateFood = foodRepository.save(duplicateFood);

        var updatedFood = Food.builder()
                .name(name+"changed")
                .start(duplicateFood.getStart())
                .end(duplicateFood.getEnd())
                .service(service)
                .price(price)
                .build();

        this.mockMvc.perform(put("/api/foods/{id}", food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFood)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void update__otherSameDay() throws Exception {
        var name = "Tagliatelle bolognese";
        var start = LocalDate.of(2020, 12, 2);
        var end = LocalDate.of(2020, 12, 6);
        var service = ServiceEnum.OTHER;
        var price = BigDecimal.valueOf(10);

        var food = Food.builder()
                .name(name)
                .start(start)
                .end(end)
                .service(service)
                .price(price)
                .build();

        food = foodRepository.save(food);

        var duplicateFood = Food.builder()
                .name(name)
                .start(LocalDate.of(2020, 12, 8))
                .end(LocalDate.of(2020, 12, 10))
                .service(service)
                .price(price)
                .build();

        duplicateFood = foodRepository.save(duplicateFood);

        var updatedFood = Food.builder()
                .name(name+"changed")
                .start(duplicateFood.getStart())
                .end(duplicateFood.getEnd())
                .service(service)
                .price(price)
                .build();

        this.mockMvc.perform(put("/api/foods/{id}", food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFood)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("name").value(name+"changed"))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("service").value(service.toString()))
                .andExpect(jsonPath("start").value(duplicateFood.getStart().toString()))
                .andExpect(jsonPath("end").value(duplicateFood.getEnd().toString()))
                .andExpect(jsonPath("price").value(price));
    }
}
