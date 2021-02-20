package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.Formula;
import be.susscrofa.api.model.Order;
import be.susscrofa.api.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_deliveryman')")
@RestController
public class OrderResource {
	
	private final OrderService orderService;

	@GetMapping("/api/clients/{clientId}/orders")
	public List<Order> findClientOrders(@PathVariable int clientId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                                        @RequestParam(required = false) Formula formula) {
		if(formula != null) {
			return orderService.getOrder(clientId, day, formula);
		}
		else {
			return orderService.getOrder(clientId, day);
		}
	}

	@PostMapping("/api/orders")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Order create(@RequestBody @Valid Order order) {
		return orderService.create(order);
	}

	@PutMapping("/api/orders/{id}")
	public Order update(@PathVariable long id, @RequestBody @Valid Order order) {
		order.setId(id);

		return orderService.update(order);
	}

	@DeleteMapping("/api/orders/{id}")
	public void delete(@PathVariable long id) {
		orderService.delete(id);
	}
}
