package be.susscrofa.api.repository;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FoodRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    FoodRepository foodRepository;

    @Test
    void findByStartAndService() {
        var start = LocalDate.of(2020, 12, 1);
        var service = ServiceEnum.DISH;

        var goodFood = Food.builder()
                .name("goodFood")
                .start(start)
                .end(LocalDate.of(2020, 12, 2))
                .service(service)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood1 = Food.builder()
                .name("otherFood1")
                .start(LocalDate.of(2020, 12, 2))
                .end(LocalDate.of(2020, 12, 2))
                .service(service)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood2 = Food.builder()
                .name("otherFood2")
                .start(start)
                .end(LocalDate.of(2020, 12, 2))
                .service(ServiceEnum.SOUP)
                .price(BigDecimal.valueOf(10))
                .build();

        foodRepository.save(goodFood);
        foodRepository.save(otherFood1);
        foodRepository.save(otherFood2);

        var result = foodRepository.findByStartAndService(start, service);
        Assertions.assertThat(result.orElseThrow()).isEqualTo(goodFood);
    }

    @Test
    void findAllByStartLessThanEqualAndEndGreaterThanEqual() {
        var goodFoods = List.of(
            Food.builder()
                .name("goodFood1")
                .start(LocalDate.of(2020, 12, 1))
                .end(LocalDate.of(2020, 12, 4))
                .service(ServiceEnum.DISH)
                .price(BigDecimal.valueOf(10))
                .build(),
            Food.builder()
                .name("goodFood2")
                .start(LocalDate.of(2020, 12, 5))
                .end(LocalDate.of(2020, 12, 5))
                .service(ServiceEnum.DESSERT)
                .price(BigDecimal.valueOf(10))
                .build(),
            Food.builder()
                .name("goodFood3")
                .start(LocalDate.of(2020, 12, 5))
                .end(LocalDate.of(2020, 12, 6))
                .service(ServiceEnum.SOUP)
                .price(BigDecimal.valueOf(10))
                .build()
        );

        var otherFood1 = Food.builder()
                .name("otherFood1")
                .start(LocalDate.of(2020, 12, 1))
                .end(LocalDate.of(2020, 12, 1))
                .service(ServiceEnum.DISH)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood2 = Food.builder()
                .name("otherFood2")
                .start(LocalDate.of(2020, 12, 6))
                .end(LocalDate.of(2020, 12, 6))
                .service(ServiceEnum.SOUP)
                .price(BigDecimal.valueOf(10))
                .build();

        foodRepository.saveAll(goodFoods);
        foodRepository.save(otherFood1);
        foodRepository.save(otherFood2);

        var result = foodRepository
                .findAllByEndGreaterThanEqualAndStartLessThanEqual(
                        LocalDate.of(2020, 12, 4),
                        LocalDate.of(2020, 12, 5));

        Assertions.assertThat(result).isEqualTo(goodFoods);
    }

    @Test
    void findAllByStartLessThanEqualAndEndGreaterThanEqualAndService() {
        var goodFoods = List.of(
                Food.builder()
                        .name("goodFood1")
                        .start(LocalDate.of(2020, 12, 1))
                        .end(LocalDate.of(2020, 12, 4))
                        .service(ServiceEnum.DISH)
                        .price(BigDecimal.valueOf(10))
                        .build(),
                Food.builder()
                        .name("goodFood2")
                        .start(LocalDate.of(2020, 12, 5))
                        .end(LocalDate.of(2020, 12, 5))
                        .service(ServiceEnum.DISH)
                        .price(BigDecimal.valueOf(10))
                        .build(),
                Food.builder()
                        .name("goodFood3")
                        .start(LocalDate.of(2020, 12, 5))
                        .end(LocalDate.of(2020, 12, 6))
                        .service(ServiceEnum.DISH)
                        .price(BigDecimal.valueOf(10))
                        .build()
        );

        var otherFood1 = Food.builder()
                .name("otherFood1")
                .start(LocalDate.of(2020, 12, 1))
                .end(LocalDate.of(2020, 12, 1))
                .service(ServiceEnum.DISH)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood2 = Food.builder()
                .name("otherFood2")
                .start(LocalDate.of(2020, 12, 6))
                .end(LocalDate.of(2020, 12, 6))
                .service(ServiceEnum.SOUP)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood3 = Food.builder()
                .name("otherFood3")
                .start(LocalDate.of(2020, 12, 5))
                .end(LocalDate.of(2020, 12, 5))
                .service(ServiceEnum.SOUP)
                .price(BigDecimal.valueOf(10))
                .build();

        var otherFood4 = Food.builder()
                .name("otherFood4")
                .start(LocalDate.of(2020, 12, 5))
                .end(LocalDate.of(2020, 12, 6))
                .service(ServiceEnum.DESSERT)
                .price(BigDecimal.valueOf(10))
                .build();

        foodRepository.saveAll(goodFoods);
        foodRepository.save(otherFood1);
        foodRepository.save(otherFood2);
        foodRepository.save(otherFood3);
        foodRepository.save(otherFood4);

        var result = foodRepository
                .findAllByEndGreaterThanEqualAndStartLessThanEqualAndService(
                        LocalDate.of(2020, 12, 4),
                        LocalDate.of(2020, 12, 5),
                        ServiceEnum.DISH);

        Assertions.assertThat(result).isEqualTo(goodFoods);
    }
}
