package be.susscrofa.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class MenuFactoryTest {

    @Test
    public void createMenuFromFoods_forTwoWeeks() {
        var model = new MenuModel();

        var result = MenuFactory
                .createMenuFromFoodsWithAlternative(model.foodsForTwoWeeksBetween6And2712,
                        LocalDate.of(2020, 12, 7),
                        LocalDate.of(2020, 12, 19));

        assertThat(result).containsExactlyInAnyOrderElementsOf(model.expectedMenusForTwoWeeksBetween7And1912);
    }

    @Test
    public void createMenuFromFoods_forTwoWeeksWithoutAlternativeDish() {
        var model = new MenuModel();

        var result = MenuFactory
                .createMenuFromFoodsWithoutAlternative(model.foodsForTwoWeeksBetween6And2712,
                        LocalDate.of(2020, 12, 7),
                        LocalDate.of(2020, 12, 19));

        assertThat(result).containsExactlyInAnyOrderElementsOf(model.expectedMenusForTwoWeeksBetween7And1912WithoutAlternative);
    }

    @Test
    public void createMenuFromFoods_forOneDay() {
        var model = new MenuModel();

        var result = MenuFactory
                .createMenuFromFoodsWithAlternative(model.foodsForTwoWeeksBetween6And2712,
                        LocalDate.of(2020, 12, 19),
                        LocalDate.of(2020, 12, 19));

        assertThat(result).containsExactlyInAnyOrderElementsOf(model.expectedMenusFor1912);
    }
}
