package be.susscrofa.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class OrderTest {

    @ParameterizedTest
    @CsvSource({
            "2020-12-01, 2020-12-01, 2020-12-01, true",
            "2020-12-01, 2020-12-01, 2020-12-02, false",
            "2020-12-01, 2020-12-03, 2020-12-01, true",
            "2020-12-01, 2020-12-03, 2020-12-02, true",
            "2020-12-01, 2020-12-03, 2020-12-03, true",
            ",2020-12-03,2020-11-30, true",
            ",2020-12-03,2020-12-04, false",
            "2020-12-01,,2020-12-03, true",
            "2020-12-01,,2020-11-30, false",
            ",,2020-12-03, true"
    })
    void checkIfMealDateAreValid(
            LocalDate foodStartDate, LocalDate foodEndDate, LocalDate orderDay,
            boolean expectedResult) {
        Order order = Order.builder()
                .day(orderDay)
                .dishId(1L)
                .dessertId(1L)
                .build();

        assertThat(order.checkIfMealsDatesAreValid((id) -> Food.builder().start(foodStartDate).end(foodEndDate).build()))
                .isEqualTo(expectedResult);
    }

    @Test
    void checkIfMealsDatesAreValid() {
        Order order = Order.builder()
                .day(LocalDate.of(2020, 12, 2))
                .dishId(1L)
                .build();

        assertThat(order.checkIfMealsDatesAreValid((id) -> Food.builder().start(LocalDate.of(2020, 12, 1)).end(LocalDate.of(2020, 12, 1)).build()))
                .isEqualTo(false);
    }

    @ParameterizedTest
    @CsvSource({
            "SOUP, true, false, false, false, true",
            "SOUP, true, true, false, false, false",
            "SOUP, false, false, false, false, false",
            "SOUP, false, true, false, false, false",
            "DISH, false, true, false, false, true",
            "DISH, true, true, true, false, false",
            "DISH, false, false, false, false, false",
            "DISH, false, false, true, false, false",
            "ALTERNATIVE_DISH, false, true, false, false, true",
            "ALTERNATIVE_DISH, true, true, true, false, false",
            "ALTERNATIVE_DISH, false, false, false, false, false",
            "ALTERNATIVE_DISH, false, false, true, false, false",
            "DESSERT, false, false, true, false, true",
            "DESSERT, true, true, false, false, false",
            "DESSERT, false, false, false, false, false",
            "DESSERT, false, true, false, false, false",
            "SOUP_DISH, true, true, false, false, true",
            "SOUP_DISH, true, true, true, false, false",
            "SOUP_DISH, false, true, true, false, false",
            "DISH_DESSERT, false, true, true, false, true",
            "DISH_DESSERT, true, true, true, false, false",
            "DISH_DESSERT, true, false, true, false, false",
            "MENU, true, true, true, false, true",
            "MENU, true, true, false, false, false",
            "MENU, true, false, true, false, false",
            "MENU, false, true, true, false, false",
            "SOUP_ALTERNATIVE_DISH, true, true, false, false, true",
            "SOUP_ALTERNATIVE_DISH, true, true, true, false, false",
            "SOUP_ALTERNATIVE_DISH, false, true, false, false, false",
            "ALTERNATIVE_DISH_DESSERT, false, true, true, false, true",
            "ALTERNATIVE_DISH_DESSERT, false, false, true, false, false",
            "ALTERNATIVE_DISH_DESSERT, false, false, false, false, false",
            "ALTERNATIVE_DISH_DESSERT, true, true, true, false, false",
            "ALTERNATIVE_MENU, true, true, true, false, true",
            "ALTERNATIVE_MENU, true, false, true, false, false",
            "ALTERNATIVE_MENU, false, false, false, false, false",
            "OTHER, false, false, false, true, true",
            "OTHER, true, true, true, false, false",
            "OTHER, false, true, false, true, false",
            "OTHER, false, false, true, false, false",
    })
    void checkIfMealsMatchMealType(Formula type, boolean soup, boolean dish, boolean dessert, boolean other,
                                   boolean expectedResult) {
        var order = Order.builder()
                .formula(type)
                .soupId(soup ? 1L : null)
                .dishId(dish ? 2L : null)
                .dessertId(dessert ? 3L : null)
                .otherId(other ? 4L : null)
                .build();

        assertThat(order.checkIfMealsMatchMealType())
                .isEqualTo(expectedResult);
    }

    @Test
    void getPriceForFood() {
        var food = Food
                .builder()
                .id(1L)
                .price(BigDecimal.valueOf(9.5))
                .vat(21)
                .build();

        var order = Order.builder()
                .dishId(food.getId())
                .formula(Formula.OTHER)
                .quantity(3)
                .build();

        var reduction = 10;

        Function<Long, Food> foodFactory = foodId -> food;

        var unitPrice = order.getUnitPrice(null, foodFactory, reduction);
        var unitPriceVatIncluded = order.getUnitPriceVatIncluded(null, foodFactory, 10);
        var totalVatExcluded = order.getTotalVatExcluded(null, foodFactory, reduction);
        var totalVatIncluded = order.getTotalVatIncluded(null, foodFactory, 10);

        assertEquals(BigDecimal.valueOf(8.55), unitPrice);
        assertEquals(BigDecimal.valueOf(10.3455), unitPriceVatIncluded);
        assertEquals(BigDecimal.valueOf(25.65), totalVatExcluded);
        assertEquals(BigDecimal.valueOf(31.0365), totalVatIncluded);
    }
}
