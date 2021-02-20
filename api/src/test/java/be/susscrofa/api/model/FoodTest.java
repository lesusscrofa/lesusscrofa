package be.susscrofa.api.model;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class FoodTest {

    @ParameterizedTest
    @CsvSource({
            "DISH,2020-12-01,2020-12-01,true",
            "DISH,2020-12-01,2020-12-02,false",
            "DISH,2020-12-03,2020-12-02,false",
            "DISH,,,false",
            "SOUP,2020-12-01,2020-12-01,true",
            "SOUP,2020-12-01,2020-12-02,false",
            "SOUP,,,false",
            "DESSERT,2020-12-01,2020-12-01,true",
            "DESSERT,2020-12-01,2020-12-02,false",
            "DESSERT,,,false",
            "ALTERNATIVE_DISH,2020-12-01,2020-12-01,true",
            "ALTERNATIVE_DISH,2020-12-01,2020-12-02,true",
            "ALTERNATIVE_DISH,2020-12-03,2020-12-02,false",
            "ALTERNATIVE_DISH,,,false",
    })
    public void testDateValidForDish(ServiceEnum service,
                                     LocalDate start, LocalDate end,
                                     boolean expected) {
        Food food = Food.builder()
                .service(service)
                .start(start)
                .end(end)
                .build();

        if(expected) {
            assertThatNoException().isThrownBy(() -> food.isValid());
        }
        else {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> food.isValid());
        }
    }

    @Test
    public void testValidPriceForOtherFood() {
        Food food = Food.builder()
                .service(ServiceEnum.OTHER)
                .price(BigDecimal.ONE)
                .build();

        assertThatNoException().isThrownBy(() -> food.isValid());
    }

    @Test
    public void testValidPriceForOtherFood_pricebellowzero_souldthrowexception() {
        Food food = Food.builder()
                .service(ServiceEnum.OTHER)
                .price(BigDecimal.valueOf(0))
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> food.isValid());

        Food foodBellowZero = Food.builder()
                .service(ServiceEnum.OTHER)
                .price(BigDecimal.valueOf(-1))
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> foodBellowZero.isValid());
    }
}
