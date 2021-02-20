package be.susscrofa.api.web.resource;

import java.util.List;

import be.susscrofa.api.model.Client;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import be.susscrofa.api.service.ClientService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
public class ClientResource {
	
	private final ClientService clientService;

	@GetMapping("/api/clients")
	public List<Client> findClients(@RequestParam String name) {
		return clientService.findClients(name);
	}

	@GetMapping("/api/clients/{clientId}")
	public Client findClient(@PathVariable int clientId) {
		return clientService.getClient(clientId);
	}

	@PostMapping("/api/clients")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Client create(@RequestBody @Valid Client client) {
		return clientService.create(client);
	}

	@PutMapping("/api/clients/{clientId}")
	public Client update(@PathVariable long clientId, @RequestBody @Valid Client client) {
		client.setId(clientId);

		return clientService.update(client);
	}
}
