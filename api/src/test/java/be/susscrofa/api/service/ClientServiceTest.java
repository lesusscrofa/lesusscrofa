package be.susscrofa.api.service;

import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.susscrofa.api.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

	@InjectMocks
	ClientService clientService;
	
	@Mock
	ClientRepository clientRepository;
	
	@Test
	public void findClients() {
		var name = "Donald";
		
		List<Client> clients = List.of();
		
		when(clientRepository.findAllByNameSurname(name))
			.thenReturn(clients);
		
		var response = clientService.findClients(name);
		
		assertThat(response).isEqualTo(clients);
	}

	@Test
	public void create() {
		Client client = Client
				.builder()
				.build();

		when(clientRepository.save(client))
				.thenReturn(client);

		var response = clientService.create(client);

		assertThat(response).isEqualTo(client);
	}

	@Test
	public void update() {
		var clientId = 1L;

		var client = Client
				.builder()
				.deliveryPosition(2)
				.id(clientId)
				.build();

		when(clientRepository.findById(clientId))
				.thenReturn(Optional.of(client));

		when(clientRepository.save(client))
				.thenReturn(client);

		var response = clientService.update(client);

		assertThat(response).isEqualTo(client);
	}

	@Test
	public void update_client_dont_exist() {
		var clientId = 1L;

		var client = Client
				.builder()
				.id(clientId)
				.build();

		when(clientRepository.findById(clientId))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> clientService.update(client)).isInstanceOf(EntityNotFoundException.class);
	}
}
