package be.susscrofa.api.repository;

import be.susscrofa.api.model.DeliveryZone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryZoneRepository extends CrudRepository<DeliveryZone, Long> {

    List<DeliveryZone> findAll();
}
