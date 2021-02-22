package be.susscrofa.api.service;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @Test
    public void getById() {
        var id = 1L;
        var food  = Optional.of(Food.builder().build());

        when(foodRepository.findById(id))
                .thenReturn(food);

        var res = foodService.getById(id);

        Assertions.assertThat(res).isEqualTo(food.orElseThrow());
    }

    @Test
    public void findByStartDateAndService() {
        var start = LocalDate.now();
        var service = ServiceEnum.OTHER;

        foodService.findByStartDateAndService(start, service);

        verify(foodRepository).findByStartAndService(start, service);
    }

    @Test
    public void findAllByStartEnd() {
        var start = LocalDate.now();
        var end = LocalDate.now();

        foodService.findAll(start, end);

        verify(foodRepository).findAllByEndGreaterThanEqualAndStartLessThanEqual(start, end);
    }

    @Test
    public void findAllByStartEndService() {
        var start = LocalDate.now();
        var end = LocalDate.now();
        var service = ServiceEnum.OTHER;

        foodService.findAll(start, end, service);

        verify(foodRepository).findAll(start, end, service);
    }

    @Test
    public void create() {
        var today = LocalDate.now();
        var service = ServiceEnum.DISH;

        var food = Food.builder()
                .service(service)
                .start(today)
                .end(today)
                .build();

        when(foodRepository.findByStartAndService(today, service))
                .thenReturn(Optional.empty());

        when(foodRepository.save(food))
                .thenReturn(food);

        var resp = foodService.create(food);

        Assertions.assertThat(resp).isEqualTo(food);
    }

    @Test
    public void create_otherSameDay() {
        var today = LocalDate.now();
        var service = ServiceEnum.OTHER;

        var food = Food.builder()
                .service(service)
                .start(today)
                .end(today)
                .price(BigDecimal.ONE)
                .build();

        when(foodRepository.save(food))
                .thenReturn(food);

        var resp = foodService.create(food);

        Assertions.assertThat(resp).isEqualTo(food);
    }

    @Test
    public void create_foodPartOfMenuAlreadyExistForSameDate() {
        var today = LocalDate.now();
        var service = ServiceEnum.DISH;

        var food = Food.builder()
                .service(service)
                .start(today)
                .end(today)
                .build();

        when(foodRepository.findByStartAndService(today, service))
                .thenReturn(Optional.of(food));

        assertThatThrownBy(() -> foodService.create(food))
            .isInstanceOf(EntityAlreadyExistException.class);
    }

    @Test
    public void create_alternativeFoodAlreadyExistForSameDate() {
        var start = LocalDate.of(2020, 12, 2);
        var end = LocalDate.of(2020, 12, 6);
        var service = ServiceEnum.ALTERNATIVE_DISH;

        var food = Food.builder()
                .service(service)
                .start(start)
                .end(end)
                .build();

        when(foodRepository.findAll(start, end, service))
                .thenReturn(List.of(food));

        assertThatThrownBy(() -> foodService.create(food))
                .isInstanceOf(EntityAlreadyExistException.class);
    }

    @Test
    public void create_dateInvalid() {
        var food = Food.builder()
                .service(ServiceEnum.DISH)
                .start(LocalDate.of(2020, 12, 1))
                .end(LocalDate.of(2020, 12, 2))
                .build();

        assertThatThrownBy(() -> foodService.create(food))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void update() {
        var today = LocalDate.now();

        var food = Food.builder()
                .id(1L)
                .service(ServiceEnum.DISH)
                .start(today)
                .end(today)
                .build();

        when(foodRepository.existsById(food.getId()))
                .thenReturn(true);

        when(foodRepository.save(food))
                .thenReturn(food);

        var resp = foodService.update(food);

        Assertions.assertThat(resp).isEqualTo(food);
    }

    @Test
    public void update_foodPartOfMenuAlreadyExistForSameDate() {
        var today = LocalDate.now();
        var service = ServiceEnum.DISH;

        var food = Food.builder()
                .id(1L)
                .service(service)
                .start(today)
                .end(today)
                .build();

        when(foodRepository.existsById(food.getId()))
                .thenReturn(true);

        when(foodRepository.findByStartAndService(today, service))
                .thenReturn(Optional.of(food.toBuilder().id(2L).build()));

        assertThatThrownBy(() -> foodService.update(food))
                .isInstanceOf(EntityAlreadyExistException.class);
    }

    @Test
    public void update_alternativeFoodAlreadyExistForSameDate() {
        var start = LocalDate.of(2020, 12, 2);
        var end = LocalDate.of(2020, 12, 6);
        var service = ServiceEnum.ALTERNATIVE_DISH;

        var food = Food.builder()
                .id(1L)
                .service(service)
                .start(start)
                .end(end)
                .build();

        when(foodRepository.existsById(food.getId()))
                .thenReturn(true);

        when(foodRepository.findAll(start, end, service))
                .thenReturn(List.of(food.toBuilder().id(2L).build()));

        assertThatThrownBy(() -> foodService.update(food))
                .isInstanceOf(EntityAlreadyExistException.class);
    }

    @Test
    public void update_foodDontExist() {
        var today = LocalDate.now();

        var food = Food.builder()
                .id(1L)
                .service(ServiceEnum.DISH)
                .start(today)
                .end(today)
                .build();

        when(foodRepository.existsById(food.getId()))
                .thenReturn(false);

        assertThatThrownBy(() -> foodService.update(food))
            .isInstanceOf(EntityNotFoundException.class);
    }
}