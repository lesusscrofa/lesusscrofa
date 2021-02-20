package be.susscrofa.api.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuFactory {

    public static List<Menu> createMenuFromFoodsWithAlternative(List<Food> foods, LocalDate start, LocalDate end) {
        return MenuFactory.createMenuFromFoods(foods, start, end, true);
    }

    public static List<Menu> createMenuFromFoodsWithoutAlternative(List<Food> foods, LocalDate start, LocalDate end) {
        return MenuFactory.createMenuFromFoods(foods, start, end, false);
    }

    private static List<Menu> createMenuFromFoods(List<Food> foods, LocalDate start, LocalDate end, boolean includeAlternativeDish) {
        return MenuFactory.createMenuFromFoods(foods, includeAlternativeDish)
                .filter(food -> food.isMenuBetween(start, end))
                    .collect(Collectors.toList());
    }

    private static Stream<Menu> createMenuFromFoods(List<Food> foods, boolean includeAlternativeDish) {
        final var soups = foods.stream()
                .filter(Food::isSoup)
                .collect(Collectors.toMap(Food::getStart, Function.identity()));

        final var desserts = foods.stream()
                .filter(Food::isDessert)
                .collect(Collectors.toMap(Food::getStart, Function.identity()));

        final var alternativeDish = foods.stream()
                .filter(f -> includeAlternativeDish)
                .filter(Food::isAlternativeDish)
                .flatMap(MenuFactory::fillAlternativeDish)
                .collect(Collectors.toMap(Food::getStart, Function.identity()));

        return foods.stream()
                .filter(Food::isMainDish)
                .map(d -> MenuFactory.createMenuFromDish(d, soups, alternativeDish, desserts));
    }

    private static Menu createMenuFromDish(Food dish, Map<LocalDate, Food> soups, Map<LocalDate, Food> alternativeDish, Map<LocalDate, Food> desserts) {
        var day = dish.getStart();

        return Menu.builder()
                .day(day)
                .soup(soups.get(day))
                .dish(dish.toBuilder().start(day).end(day).build())
                .alternativeDish(alternativeDish.get(day))
                .dessert(desserts.get(day))
                .build();
    }

    private static Stream<Food> fillAlternativeDish(Food alternativeDish) {
        return alternativeDish.getStart().datesUntil(alternativeDish.getEnd().plusDays(1))
                .map(day -> alternativeDish.toBuilder()
                        .start(day)
                        .end(day).build());
    }


}
