package be.susscrofa.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meal_order")
public class Order {

	private final static String ORDER_QUANTITY_INVALID = "Can't create order with zero quantity";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "client must be not null")
	private Long clientId;

	@NotNull(message = "day must be not null")
	private LocalDate day;

	private Long soupId;

	private Long dishId;

	private Long dessertId;

	private Long otherId;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "type must be not null")
	private Formula formula;

	@NotNull(message = "quantity must be not null")
	private Integer quantity;

	@NotNull(message = "delivery must not be null")
	private Boolean delivery;

	public boolean checkIfMealsMatchMealType() {
		return switch (this.formula) {
			case MENU, ALTERNATIVE_MENU -> this.soupId != null && this.dishId != null && this.dessertId != null;
			case SOUP_DISH, SOUP_ALTERNATIVE_DISH -> this.soupId != null && this.dishId != null && this.dessertId == null;
			case DISH_DESSERT, ALTERNATIVE_DISH_DESSERT -> this.soupId == null && this.dishId != null && this.dessertId != null;
			case SOUP -> this.soupId != null && this.dishId == null && this.dessertId == null;
			case DISH, ALTERNATIVE_DISH -> this.soupId == null && this.dishId != null && this.dessertId == null;
			case DESSERT -> this.soupId == null && this.dishId == null && this.dessertId != null;
			case OTHER -> this.soupId == null && this.dishId == null && this.dessertId == null && this.otherId != null;
			default -> throw new RuntimeException(String.format("Unknown meal type %s", this.formula));
		};
	}

	public BigDecimal getUnitPrice(BiFunction<Formula, LocalDate, FormulaPrice> formulaPriceFactory, Function<Long, Food> foodFactory, int reduction) {
		var priceWithoutReduction = formula.equals(Formula.OTHER) ? foodFactory.apply(dishId).getPrice() : formulaPriceFactory.apply(formula, day).getPrice();

		return priceWithoutReduction.multiply(BigDecimal.valueOf(100 - reduction).divide(BigDecimal.valueOf(100)));
	}

	public BigDecimal getUnitPriceVatIncluded(BiFunction<Formula, LocalDate, FormulaPrice> formulaPriceFactory, Function<Long, Food> foodFactory, int reduction) {
		int vat = formula.equals(Formula.OTHER) ? foodFactory.apply(dishId).getVat() : formulaPriceFactory.apply(formula, day).getVat();

		return getUnitPrice(formulaPriceFactory, foodFactory, reduction)
				.multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
	}

	public BigDecimal getTotalVatExcluded(BiFunction<Formula, LocalDate, FormulaPrice> formulaPriceFactory, Function<Long, Food> foodFactory, int reduction) {
		return getUnitPrice(formulaPriceFactory, foodFactory, reduction)
				.multiply(BigDecimal.valueOf(quantity));
	}

	public BigDecimal getTotalVatIncluded(BiFunction<Formula, LocalDate, FormulaPrice> formulaPriceFactory, Function<Long, Food> foodFactory, int reduction) {
		int vat = formula.equals(Formula.OTHER) ? foodFactory.apply(dishId).getVat() : formulaPriceFactory.apply(formula, day).getVat();

		return getTotalVatExcluded(formulaPriceFactory, foodFactory, reduction)
				.multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))));
	}

	@Transient
	public void isValid() {
		if(getQuantity() <= 0) {
			throw new IllegalArgumentException(ORDER_QUANTITY_INVALID);
		}
	}

	public boolean checkIfMealsDatesAreValid(Function<Long, Food> foodFactory) {
		return this.checkIfMealDateIsValid(foodFactory.apply(this.soupId))
				&& this.checkIfMealDateIsValid(foodFactory.apply(this.dishId))
				&& this.checkIfMealDateIsValid(foodFactory.apply(this.dessertId));
	}

	private boolean checkIfMealDateIsValid(Food meal) {
		return meal == null
				|| (this.getDay().equals(meal.getStart()) || this.getDay().equals(meal.getEnd())
				|| ((meal.getStart() == null || this.getDay().isAfter(meal.getStart()))
						&& (meal.getEnd() == null || this.getDay().isBefore(meal.getEnd()))));
	}
}
