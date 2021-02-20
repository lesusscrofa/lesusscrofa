package be.susscrofa.api.web.resource;

import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.service.DeliveryZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class DeliveryZonesResource {
	
	private final DeliveryZoneService deliveryZoneService;

	@GetMapping("/api/deliveryZones")
	public List<DeliveryZone> findDeliveryMans() {
		return deliveryZoneService.findAll();
	}

	@GetMapping("/api/deliveryZones/{deliveryZoneId}")
	public DeliveryZone getDeliveryZone(@PathVariable int deliveryZoneId) {
		return deliveryZoneService.get(deliveryZoneId);
	}

	@PostMapping("/api/deliveryZones")
	@ResponseStatus(code = HttpStatus.CREATED)
	public DeliveryZone create(@RequestBody @Valid DeliveryZone deliveryZone) {
		return deliveryZoneService.create(deliveryZone);
	}

	@PutMapping("/api/deliveryZones/{deliveryZonesId}")
	public DeliveryZone update(@PathVariable Long deliveryZonesId, @RequestBody @Valid DeliveryZone deliveryZone) {
		deliveryZone.setId(deliveryZonesId);

		return deliveryZoneService.update(deliveryZone);
	}

	@DeleteMapping("/api/deliveryZones/{deliveryZonesId}")
	public void delete(@PathVariable int deliveryZonesId) {
		deliveryZoneService.delete(deliveryZonesId);
	}
}
