package be.susscrofa.api.resource;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.repository.DeliveryZoneRepository;
import be.susscrofa.api.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import be.susscrofa.api.repository.ClientRepository;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientResourceTest extends AbstractResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private DeliveryManRepository deliveryManRepository;

	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EntityManager em;
	
	@Test
	void findClients() throws Exception {
		var firstName = "John";
		var lastName = "Doeuf";
		var deliveryStreet = "Rue de la bosse 135";
		var deliveryZone = createDeliveryZone();
		var deliveryPhone = "0472789987";
		var billingStreet = "Route de Gembloux 135";
		var billingZipCode = 5000;
		var billingCity = "Namur";
		var billingPhone = "0472789936";
		var reduction = 20;
		var deliveryPosition = 3;


		clientRepository.save(Client.builder()
				.firstName(firstName)
				.lastName(lastName)
				.deliveryStreet(deliveryStreet)
				.deliveryZoneId(deliveryZone.getId())
				.deliveryPhone(deliveryPhone)
				.billingStreet(billingStreet)
				.billingZipCode(billingZipCode)
				.billingCity(billingCity)
				.billingPhone(billingPhone)
				.reduction(reduction)
				.deliveryPosition(deliveryPosition)
				.build());
		
		this.mockMvc.perform(get("/api/clients")
				.param("name", "John"))
			.andDo(print())
			.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[0].firstName").value(firstName))
				.andExpect(jsonPath("$[0].lastName").value(lastName))
				.andExpect(jsonPath("$[0].deliveryStreet").value(deliveryStreet))
				.andExpect(jsonPath("$[0].deliveryZoneId").value(deliveryZone.getId()))
				.andExpect(jsonPath("$[0].deliveryPhone").value(deliveryPhone))
				.andExpect(jsonPath("$[0].billingStreet").value(billingStreet))
				.andExpect(jsonPath("$[0].billingZipCode").value(billingZipCode))
				.andExpect(jsonPath("$[0].billingCity").value(billingCity))
				.andExpect(jsonPath("$[0].billingPhone").value(billingPhone))
				.andExpect(jsonPath("$[0].reduction").value(reduction))
				.andExpect(jsonPath("$[0].deliveryPosition").value(deliveryPosition));
	}

	@Test
	public void create() throws Exception {
		var firstName = "John";
		var lastName = "Doeuf";
		var deliveryStreet = "Rue de la bosse 135";
		var deliveryZone = createDeliveryZone();
		var deliveryPhone = "0472789987";
		var billingStreet = "Route de Gembloux 135";
		var billingZipCode = 5000;
		var billingCity = "Namur";
		var billingPhone = "0472789936";
		var reduction = 20;
		var deliveryPosition = 3;

		var client = Client.builder()
				.firstName(firstName)
				.lastName(lastName)
				.deliveryStreet(deliveryStreet)
				.deliveryZoneId(deliveryZone.getId())
				.deliveryPhone(deliveryPhone)
				.billingStreet(billingStreet)
				.billingZipCode(billingZipCode)
				.billingCity(billingCity)
				.billingPhone(billingPhone)
				.reduction(reduction)
				.deliveryPosition(deliveryPosition)
				.build();

		this.mockMvc.perform(post("/api/clients")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(client)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(firstName))
				.andExpect(jsonPath("lastName").value(lastName))
				.andExpect(jsonPath("deliveryStreet").value(deliveryStreet))
				.andExpect(jsonPath("deliveryZoneId").value(deliveryZone.getId()))
				.andExpect(jsonPath("deliveryPhone").value(deliveryPhone))
				.andExpect(jsonPath("billingStreet").value(billingStreet))
				.andExpect(jsonPath("billingZipCode").value(billingZipCode))
				.andExpect(jsonPath("billingCity").value(billingCity))
				.andExpect(jsonPath("billingPhone").value(billingPhone))
				.andExpect(jsonPath("reduction").value(reduction))
				.andExpect(jsonPath("deliveryPosition").value(1));
	}

	@Test
	public void create_missingProperties() throws Exception {
		var firstName = "John";

		var client = Client.builder()
				.firstName(firstName)
				.build();

		this.mockMvc.perform(post("/api/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(client)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("errors", hasItem("lastName missing")))
				.andExpect(jsonPath("errors", hasItem("delivery zone missing")));
	}

	@Test
	public void update() throws Exception {
		var firstName = "John";
		var lastName = "Doeuf";
		var deliveryStreet = "Rue de la bosse 135";
		var deliveryZone = createDeliveryZone();
		var deliveryPhone = "0472789987";
		var billingStreet = "Route de Gembloux 135";
		var billingZipCode = 5000;
		var billingCity = "Namur";
		var billingPhone = "0472789936";
		var reduction = 20;
		var deliveryPosition = 3;

		var client = Client.builder()
				.firstName(firstName)
				.lastName(lastName)
				.deliveryPosition(deliveryPosition)
				.deliveryZoneId(deliveryZone.getId())
				.build();

		client = clientRepository.save(client);

		var clientDto = Client.builder()
				.firstName(firstName + "updated")
				.lastName(lastName + "updated")
				.deliveryStreet(deliveryStreet)
				.deliveryZoneId(deliveryZone.getId())
				.deliveryPhone(deliveryPhone)
				.billingStreet(billingStreet)
				.billingZipCode(billingZipCode)
				.billingCity(billingCity)
				.billingPhone(billingPhone)
				.reduction(reduction)
				.deliveryPosition(deliveryPosition)
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(firstName + "updated"))
				.andExpect(jsonPath("lastName").value(lastName + "updated"))
				.andExpect(jsonPath("deliveryStreet").value(deliveryStreet))
				.andExpect(jsonPath("deliveryZoneId").value(deliveryZone.getId()))
				.andExpect(jsonPath("deliveryPhone").value(deliveryPhone))
				.andExpect(jsonPath("billingStreet").value(billingStreet))
				.andExpect(jsonPath("billingZipCode").value(billingZipCode))
				.andExpect(jsonPath("billingCity").value(billingCity))
				.andExpect(jsonPath("billingPhone").value(billingPhone))
				.andExpect(jsonPath("reduction").value(reduction))
				.andExpect(jsonPath("deliveryPosition").value(deliveryPosition));
	}

	@Test
	public void update_giveMorePriorDeliveryPosition() throws Exception {
		var deliveryZone = createDeliveryZone();

		var client1 = Client.builder()
				.firstName("firstName1")
				.lastName("lastName1")
				.deliveryZoneId(deliveryZone.getId())
				.deliveryPosition(1)
				.build();
		client1 = clientRepository.save(client1);

		var client2 = Client.builder()
				.firstName("firstName2")
				.lastName("lastName2")
				.deliveryZoneId(deliveryZone.getId())
				.deliveryPosition(2)
				.build();
		client2 = clientRepository.save(client2);

		var client3 = Client.builder()
				.firstName("firstName3")
				.lastName("lastName3")
				.deliveryPosition(3)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client3 = clientRepository.save(client3);

		var client4 = Client.builder()
				.firstName("firstName4")
				.lastName("lastName4")
				.deliveryPosition(4)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client4 = clientRepository.save(client4);

		var updatedClient = Client.builder()
				.firstName(client3.getFirstName())
				.lastName(client3.getLastName())
				.deliveryPosition(2)
				.deliveryZoneId(deliveryZone.getId())
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", client3.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedClient)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(client3.getFirstName()))
				.andExpect(jsonPath("lastName").value(client3.getLastName()))
				.andExpect(jsonPath("deliveryPosition").value(2));

		Assertions.assertThat(clientRepository.findById(client1.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(1);
		Assertions.assertThat(clientRepository.findById(client2.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(3);
		Assertions.assertThat(clientRepository.findById(client3.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(2);
		Assertions.assertThat(clientRepository.findById(client4.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(4);
	}

	@Test
	public void update_givBestPriorDeliveryPosition() throws Exception {
		var deliveryZone = createDeliveryZone();

		var client1 = Client.builder()
				.firstName("firstName1")
				.lastName("lastName1")
				.deliveryPosition(1)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client1 = clientRepository.save(client1);

		var client2 = Client.builder()
				.firstName("firstName2")
				.lastName("lastName2")
				.deliveryPosition(2)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client2 = clientRepository.save(client2);

		var client3 = Client.builder()
				.firstName("firstName3")
				.lastName("lastName3")
				.deliveryPosition(3)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client3 = clientRepository.save(client3);

		var client4 = Client.builder()
				.firstName("firstName4")
				.lastName("lastName4")
				.deliveryPosition(4)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client4 = clientRepository.save(client4);

		var updatedClient = Client.builder()
				.firstName(client3.getFirstName())
				.lastName(client3.getLastName())
				.deliveryPosition(1)
				.deliveryZoneId(deliveryZone.getId())
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", client3.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedClient)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(client3.getFirstName()))
				.andExpect(jsonPath("lastName").value(client3.getLastName()))
				.andExpect(jsonPath("deliveryPosition").value(1));

		Assertions.assertThat(clientRepository.findById(client1.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(2);
		Assertions.assertThat(clientRepository.findById(client2.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(3);
		Assertions.assertThat(clientRepository.findById(client3.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(1);
		Assertions.assertThat(clientRepository.findById(client4.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(4);
	}

	@Test
	public void update_giveLessPriorDeliveryPosition() throws Exception {
		var deliveryZone = createDeliveryZone();

		var client1 = Client.builder()
				.firstName("firstName1")
				.lastName("lastName1")
				.deliveryPosition(1)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client1 = clientRepository.save(client1);

		var client2 = Client.builder()
				.firstName("firstName2")
				.lastName("lastName2")
				.deliveryPosition(2)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client2 = clientRepository.save(client2);

		var client3 = Client.builder()
				.firstName("firstName3")
				.lastName("lastName3")
				.deliveryPosition(3)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client3 = clientRepository.save(client3);

		var client4 = Client.builder()
				.firstName("firstName4")
				.lastName("lastName4")
				.deliveryPosition(4)
				.deliveryZoneId(deliveryZone.getId())
				.build();
		client4 = clientRepository.save(client4);

		var updatedClient = Client.builder()
				.firstName(client1.getFirstName())
				.lastName(client1.getLastName())
				.deliveryPosition(3)
				.deliveryZoneId(deliveryZone.getId())
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", client1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedClient)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(client1.getFirstName()))
				.andExpect(jsonPath("lastName").value(client1.getLastName()))
				.andExpect(jsonPath("deliveryPosition").value(3));

		Assertions.assertThat(clientRepository.findById(client1.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(3);
		Assertions.assertThat(clientRepository.findById(client2.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(1);
		Assertions.assertThat(clientRepository.findById(client3.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(2);
		Assertions.assertThat(clientRepository.findById(client4.getId()).orElseThrow().getDeliveryPosition())
				.isEqualTo(4);
	}

	@Test
	public void update_personDontExist() throws Exception {
		var firstName = "John";
		var lastName = "Doeuf";
		var deliveryZone = createDeliveryZone();

		var clientDto = Client.builder()
				.firstName(firstName)
				.lastName(lastName)
				.deliveryZoneId(deliveryZone.getId())
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDto)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void update_missingParameters() throws Exception {
		var street = "Rue du tileuil 135";

		var clientDto = Client.builder()
				.deliveryStreet(street)
				.build();

		this.mockMvc.perform(put("/api/clients/{clientId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("firstName missing")))
				.andExpect(jsonPath("$.errors", hasItem("lastName missing")));
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
				.name("boucle1")
				.build());
	}
}
