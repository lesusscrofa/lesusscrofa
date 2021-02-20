package be.susscrofa.api.repository;

import java.util.List;

import be.susscrofa.api.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
	
	@Query("select c from Client c where lower(c.firstName || c.lastName) like lower(concat('%', :param,'%'))")
	List<Client> findAllByNameSurname(@Param("param") String param);

	@Query("select coalesce(max(c.deliveryPosition),0) from Client c")
	int getMaxClientDeliveryPositionPosition();

	@Modifying
	@Query("UPDATE Client c SET c.deliveryPosition = c.deliveryPosition + 1 WHERE c.deliveryPosition >= :newDeliveryPosition and c.deliveryPosition < :oldDeliveryPosition")
	@Transactional(propagation = Propagation.MANDATORY)
	void incrementClientDeliveryPosition(int newDeliveryPosition, int oldDeliveryPosition);

	@Modifying
	@Query("UPDATE Client c SET c.deliveryPosition = c.deliveryPosition - 1 WHERE c.deliveryPosition > :oldDeliveryPosition and c.deliveryPosition <= :newDeliveryPosition")
	@Transactional(propagation = Propagation.MANDATORY)
	void decrementClientDeliveryPosition(int newDeliveryPosition, int oldDeliveryPosition);
}
