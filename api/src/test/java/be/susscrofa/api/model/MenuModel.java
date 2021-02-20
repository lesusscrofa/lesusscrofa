package be.susscrofa.api.model;

import java.time.LocalDate;
import java.util.List;

public class MenuModel {

    private final Food soup6 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 6))
            .end(LocalDate.of(2020, 12, 6))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish6 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 6))
            .end(LocalDate.of(2020, 12, 6))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert6 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 6))
            .end(LocalDate.of(2020, 12, 6))
            .service(ServiceEnum.DESSERT)
            .build();

    private final Food soup7 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 7))
            .end(LocalDate.of(2020, 12, 7))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish7 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 7))
            .end(LocalDate.of(2020, 12, 7))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert7 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 7))
            .end(LocalDate.of(2020, 12, 7))
            .service(ServiceEnum.DESSERT)
            .build();

    private final Food soup9 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 9))
            .end(LocalDate.of(2020, 12, 9))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish9 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 9))
            .end(LocalDate.of(2020, 12, 9))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert9 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 9))
            .end(LocalDate.of(2020, 12, 9))
            .service(ServiceEnum.DESSERT)
            .build();

    private final Food alternativeDish7to9 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 7))
            .end(LocalDate.of(2020, 12, 9))
            .service(ServiceEnum.ALTERNATIVE_DISH)
            .build();

    private final Food soup15 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 15))
            .end(LocalDate.of(2020, 12, 15))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish15 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 15))
            .end(LocalDate.of(2020, 12, 15))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert15 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 15))
            .end(LocalDate.of(2020, 12, 15))
            .service(ServiceEnum.DESSERT)
            .build();

    private final Food soup19 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 19))
            .end(LocalDate.of(2020, 12, 19))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish19 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 19))
            .end(LocalDate.of(2020, 12, 19))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert19 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 19))
            .end(LocalDate.of(2020, 12, 19))
            .service(ServiceEnum.DESSERT)
            .build();

    private final Food alternativeDish15to19 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 15))
            .end(LocalDate.of(2020, 12, 19))
            .service(ServiceEnum.ALTERNATIVE_DISH)
            .build();


    private final Food alternativeDish20to27 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 20))
            .end(LocalDate.of(2020, 12, 27))
            .service(ServiceEnum.ALTERNATIVE_DISH)
            .build();

    private final Food soup20 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 20))
            .end(LocalDate.of(2020, 12, 20))
            .service(ServiceEnum.SOUP)
            .build();
    private final Food dish20 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 20))
            .end(LocalDate.of(2020, 12, 20))
            .service(ServiceEnum.DISH)
            .build();
    private final Food dessert20 = Food.builder()
            .name("")
            .start(LocalDate.of(2020, 12, 20))
            .end(LocalDate.of(2020, 12, 20))
            .service(ServiceEnum.DESSERT)
            .build();

    public final List<Food> foodsForTwoWeeksBetween6And2712 = List.of(
            soup6, dish6, dessert6,
            soup7, dish7, dessert7,
            soup9, dish9, dessert9,
            alternativeDish7to9,
            soup15, dish15, dessert15,
            soup19, dish19, dessert19,
            alternativeDish15to19,
            soup20, dish20, dessert20,
            alternativeDish20to27);

    public final List<Menu> expectedMenusForTwoWeeksBetween7And1912 = List.of(
            Menu.builder()
                    .day(soup7.getStart())
                    .soup(soup7)
                    .dish(dish7)
                    .alternativeDish(alternativeDish7to9.toBuilder().start(soup7.getStart()).end(soup7.getStart()).build())
                    .dessert(dessert7)
                    .build(),
            Menu.builder()
                    .day(soup9.getStart())
                    .soup(soup9)
                    .dish(dish9)
                    .alternativeDish(alternativeDish7to9.toBuilder().start(soup9.getStart()).end(soup9.getStart()).build())
                    .dessert(dessert9)
                    .build(),

            Menu.builder()
                    .day(soup15.getStart())
                    .soup(soup15)
                    .dish(dish15)
                    .alternativeDish(alternativeDish15to19.toBuilder().start(soup15.getStart()).end(soup15.getStart()).build())
                    .dessert(dessert15)
                    .build(),
            Menu.builder()
                    .day(soup19.getStart())
                    .soup(soup19)
                    .dish(dish19)
                    .alternativeDish(alternativeDish15to19.toBuilder().start(soup19.getStart()).end(soup19.getStart()).build())
                    .dessert(dessert19)
                    .build()
    );

    public final List<Menu> expectedMenusForTwoWeeksBetween7And1912WithoutAlternative = List.of(
            Menu.builder()
                    .day(soup7.getStart())
                    .soup(soup7)
                    .dish(dish7)
                    .dessert(dessert7)
                    .build(),
            Menu.builder()
                    .day(soup9.getStart())
                    .soup(soup9)
                    .dish(dish9)
                    .dessert(dessert9)
                    .build(),

            Menu.builder()
                    .day(soup15.getStart())
                    .soup(soup15)
                    .dish(dish15)
                    .dessert(dessert15)
                    .build(),
            Menu.builder()
                    .day(soup19.getStart())
                    .soup(soup19)
                    .dish(dish19)
                    .dessert(dessert19)
                    .build()
    );

    public final List<Menu> expectedMenusFor1912 = List.of(
            Menu.builder()
                    .day(soup19.getStart())
                    .soup(soup19)
                    .dish(dish19)
                    .alternativeDish(alternativeDish15to19.toBuilder().start(soup19.getStart()).end(soup19.getStart()).build())
                    .dessert(dessert19)
                    .build()
    );
}
