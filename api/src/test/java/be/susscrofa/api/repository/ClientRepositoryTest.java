package be.susscrofa.api.repository;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;


public class ClientRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private DeliveryManRepository deliveryManRepository;

	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;

	@Autowired
	ClientRepository clientRepository;
	
	@Test
	void testFindAllByNameAndSurname() {
		var deliveryZone = createDeliveryZone();

		var client1 = Client.builder()
				.id(1L)
				.firstName("Donald")
				.lastName("Duck")
				.deliveryZoneId(deliveryZone.getId())
				.build();
		
		var client2 = Client.builder()
				.id(2L)
				.firstName("Axel")
				.lastName("Aire")
				.deliveryZoneId(deliveryZone.getId())
				.build();
		
		var client3 = Client.builder()
				.id(2L)
				.firstName("Marc")
				.lastName("Onald")
				.deliveryZoneId(deliveryZone.getId())
				.build();

		client1 = clientRepository.save(client1);
		clientRepository.save(client2);
		client3 = clientRepository.save(client3);
		
		var clients = clientRepository.findAllByNameSurname("onald");
		
		assertThat(clients.size()).isEqualTo(2);
		assertThat(clients.get(0)).isEqualTo(client1);
		assertThat(clients.get(1)).isEqualTo(client3);
	}

	private DeliveryZone createDeliveryZone() {
		var deliveryMan = this.deliveryManRepository.save(DeliveryMan
				.builder()
				.firstName("first")
				.lastName("last")
				.build());

		return this.deliveryZoneRepository.save(DeliveryZone
				.builder()
				.deliveryManId(deliveryMan.getId())
				.name("zone1")
				.build());
	}
}
