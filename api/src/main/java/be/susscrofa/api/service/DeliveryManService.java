package be.susscrofa.api.service;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Service
public class DeliveryManService {

    protected static final String DELIVERY_MAN_NOT_FOUND = "There is no delivery man with id %d";

    private final DeliveryManRepository deliveryManRepository;

    public List<DeliveryMan> findAll() {
        return deliveryManRepository.findAll();
    }

    public DeliveryMan get(long id) {
        return deliveryManRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(DELIVERY_MAN_NOT_FOUND, id)));
    }

    public DeliveryMan create(@NotNull @Valid DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }

    public DeliveryMan update(@NotNull @Valid DeliveryMan deliveryMan) {
        if(!deliveryManRepository.existsById(deliveryMan.getId())) {
            throw new EntityNotFoundException(String.format(DELIVERY_MAN_NOT_FOUND, deliveryMan.getId()));
        }

        return deliveryManRepository.save(deliveryMan);
    }

    public void delete(long deliveryManId) {
        if(!deliveryManRepository.existsById(deliveryManId)) {
            throw new EntityNotFoundException(String.format(DELIVERY_MAN_NOT_FOUND, deliveryManId));
        }

        deliveryManRepository.deleteById(deliveryManId);
    }
}
