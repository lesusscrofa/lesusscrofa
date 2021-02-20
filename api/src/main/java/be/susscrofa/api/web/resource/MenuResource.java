package be.susscrofa.api.web.resource;

import java.time.LocalDate;
import java.util.List;

import be.susscrofa.api.model.Menu;
import be.susscrofa.api.service.MenuService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class MenuResource {
	
	private final MenuService menuService;

	@GetMapping("/api/menus")
	public List<Menu> findMenus(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
			@RequestParam(required = false, defaultValue = "true") Boolean includeAlternative) {


		return menuService.findAll(start, end, includeAlternative);
	}

	@PostMapping("/api/menus")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Menu create(@RequestBody @Valid Menu menu) {
		return menuService.create(menu);
	}

	@PutMapping("/api/menus/{day}")
	public Menu update(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day, @RequestBody Menu menu) {
		menu.setDay(day);

		return menuService.update(menu);
	}
}
