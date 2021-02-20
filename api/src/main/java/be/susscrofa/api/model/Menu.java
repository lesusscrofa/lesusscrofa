package be.susscrofa.api.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

	@NotNull(message = "day is missing")
	private LocalDate day;

	private Food soup;

	private Food dish;

	private Food alternativeDish;

	private Food dessert;

	public boolean isMenuBetween(LocalDate start, LocalDate end) {
		return day.equals(start) || day.equals(end) ||
				(day.isAfter(start) && day.isBefore(end));
	}
}
