package be.susscrofa.api.service;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.repository.FoodRepository;
import be.susscrofa.api.service.exception.EntityAlreadyExistException;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FoodService {

    public static final String FOOD_NOT_FOUND = "Food with id %d not found";

    public static final String FOOD_ALREADY_CREATED = "Food with id %d already created";

    private final FoodRepository foodRepository;

    public Food getById(long id) {
        return foodRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(FOOD_NOT_FOUND, id)));
    }

    public Optional<Food> findByStartDateAndService(@NotNull LocalDate startDate, @NotNull ServiceEnum service) {
        return foodRepository.findByStartAndService(startDate, service);
    }

    public List<Food> findAll(@NotNull LocalDate start, @NotNull LocalDate end) {
        return foodRepository.findAllByEndGreaterThanEqualAndStartLessThanEqual(start, end);
    }

    public List<Food> findAll(@NotNull LocalDate start, @NotNull LocalDate end, @NotNull ServiceEnum service) {
        return foodRepository.findAll(start, end, service);
    }

    public List<Food> findAll(@NotNull LocalDate start, @NotNull LocalDate end, @NotNull ServiceEnum service, String partialName) {
        return foodRepository.findAll(start, end, service, partialName);
    }

    public List<Food> findAll(@NotNull ServiceEnum service) {
        return foodRepository.findAllByService(service);
    }

    public Food create(@NotNull Food food) {
        findFoodPartOfMenuWithMatchingDate(food)
                .ifPresent(f -> {
                    throw new EntityAlreadyExistException(String.format(FOOD_ALREADY_CREATED, food.getId()));
                });

        return save(food);
    }

    public Food update(@NotNull Food food) {
        if(!foodRepository.existsById(food.getId())) {
            throw new EntityNotFoundException(String.format(FOOD_NOT_FOUND, food.getId()));
        }

        findFoodPartOfMenuWithMatchingDate(food)
                .filter(f -> !f.getId().equals(food.getId()))
                .ifPresent(f -> {
                    throw new EntityAlreadyExistException(String.format(FOOD_ALREADY_CREATED, food.getId()));
                });

        return save(food);
    }

    private Food save(@NotNull Food food) {
        food.isValid();

        return this.foodRepository.save(food);
    }

    private Optional<Food> findFoodPartOfMenuWithMatchingDate(Food food) {
        if(food.isMainDish() || food.isSoup() || food.isDessert()) {
            return foodRepository.findByStartAndService(food.getStart(), food.getService());
        }
        else if(food.isAlternativeDish()) {
            return foodRepository
                    .findAll(
                            food.getStart(),
                            food.getEnd(),
                            ServiceEnum.ALTERNATIVE_DISH)
                    .stream()
                    .findAny();
        }

        return Optional.empty();
    }

    public void delete(@NotNull Long id) {
        this.foodRepository.deleteById(id);
    }
}
