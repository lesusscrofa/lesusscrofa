package be.susscrofa.api.service;

import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.repository.DeliveryZoneRepository;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Service
public class DeliveryZoneService {

    protected static final String DELIVERY_ZONE_NOT_FOUND = "There is no delivery zone with id %d";

    private final DeliveryZoneRepository deliveryZoneRepository;

    public List<DeliveryZone> findAll() {
        return deliveryZoneRepository.findAll();
    }

    public DeliveryZone get(long id) {
        return deliveryZoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(DELIVERY_ZONE_NOT_FOUND, id)));
    }

    public DeliveryZone create(@NotNull @Valid DeliveryZone deliveryZone) {
        return deliveryZoneRepository.save(deliveryZone);
    }

    public DeliveryZone update(@NotNull @Valid DeliveryZone deliveryZone) {
        if(!deliveryZoneRepository.existsById(deliveryZone.getId())) {
            throw new EntityNotFoundException(String.format(DELIVERY_ZONE_NOT_FOUND, deliveryZone.getId()));
        }

        return deliveryZoneRepository.save(deliveryZone);
    }

    public void delete(long deliveryZoneId) {
        if(!deliveryZoneRepository.existsById(deliveryZoneId)) {
            throw new EntityNotFoundException(String.format(DELIVERY_ZONE_NOT_FOUND, deliveryZoneId));
        }

        deliveryZoneRepository.deleteById(deliveryZoneId);
    }
}
