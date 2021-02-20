package be.susscrofa.api.service;

import java.util.List;

import be.susscrofa.api.service.exception.EntityNotFoundException;
import be.susscrofa.api.model.Client;
import org.springframework.stereotype.Service;

import be.susscrofa.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientService {

	private static final String CLIENT_DONT_EXIST = "Person with id %d not found";

	private final ClientRepository clientRepository;

	public Client getClient(long id) {
		return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(CLIENT_DONT_EXIST, id)));
	}

	public List<Client> findClients(String name) {
		return clientRepository.findAllByNameSurname(name);
	}

	public Client create(Client client) {
		int deliveryPosition = clientRepository.getMaxClientDeliveryPositionPosition() + 1;

		client.setDeliveryPosition(deliveryPosition);

		return clientRepository.save(client);
	}

	@Transactional
	public Client update(Client client) {
		var existingClient = clientRepository.findById(client.getId())
				.orElseThrow(() -> new EntityNotFoundException(String.format(CLIENT_DONT_EXIST, client.getId())));

		updateClientDeliveryPosition(existingClient, client);

		return clientRepository.save(client);
	}

	private void updateClientDeliveryPosition(Client oldClient, Client newClient) {
		if(newClient.getDeliveryPosition() > oldClient.getDeliveryPosition()) {
			clientRepository.decrementClientDeliveryPosition(newClient.getDeliveryPosition(), oldClient.getDeliveryPosition());
		}
		else if(newClient.getDeliveryPosition() < oldClient.getDeliveryPosition()) {
			clientRepository.incrementClientDeliveryPosition(newClient.getDeliveryPosition(), oldClient.getDeliveryPosition());
		}
	}
}
