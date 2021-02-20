package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.service.DeliveryManService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class DeliveryMansResource {
	
	private final DeliveryManService deliveryManService;

	@GetMapping("/api/deliveryMans")
	public List<DeliveryMan> findDeliveryMans() {
		return deliveryManService.findAll();
	}

	@GetMapping("/api/deliveryMans/{deliveryManId}")
	public DeliveryMan getDeliveryMan(@PathVariable int deliveryManId) {
		return deliveryManService.get(deliveryManId);
	}

	@PostMapping("/api/deliveryMans")
	@ResponseStatus(code = HttpStatus.CREATED)
	public DeliveryMan create(@RequestBody @Valid DeliveryMan deliveryMan) {
		return deliveryManService.create(deliveryMan);
	}

	@PutMapping("/api/deliveryMans/{deliveryManId}")
	public DeliveryMan update(@PathVariable long deliveryManId, @RequestBody @Valid DeliveryMan deliveryMan) {
		deliveryMan.setId(deliveryManId);

		return deliveryManService.update(deliveryMan);
	}

	@DeleteMapping("/api/deliveryMans/{deliveryManId}")
	public void delete(@PathVariable int deliveryManId) {
		deliveryManService.delete(deliveryManId);
	}
}
