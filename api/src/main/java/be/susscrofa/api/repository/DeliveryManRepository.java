package be.susscrofa.api.repository;

import be.susscrofa.api.model.DeliveryMan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryManRepository extends CrudRepository<DeliveryMan, Long> {

    List<DeliveryMan> findAll();
}
