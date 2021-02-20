package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Food;
import be.susscrofa.api.model.ServiceEnum;
import be.susscrofa.api.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_deliveryman')")
@RestController
public class FoodResource {

    private final FoodService foodService;

    @GetMapping("/api/foods")
    public List<Food> findFoods(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam ServiceEnum service) {
        if(start != null && end != null) {
            return foodService.findAll(start, end, service);
        }
        else if(start == null && end == null) {
            return foodService.findAll(service);
        }
        else {
            throw new IllegalArgumentException("if start date or end date is present, both parameters must be provided");
        }
    }

    @GetMapping("/api/foods/{foodId}")
    public Food findFood(@PathVariable Integer foodId) {
        return foodService.getById(foodId);
    }

    @PostMapping("/api/foods")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Food create(@RequestBody @Valid Food food) {
        return foodService.create(food);
    }

    @PutMapping("/api/foods/{id}")
    public Food update(@PathVariable long id, @RequestBody @Valid Food food) {
        food.setId(id);

        return foodService.update(food);
    }

    @DeleteMapping("/api/foods/{id}")
    public void delete(@PathVariable long id) {
        foodService.delete(id);
    }
}
