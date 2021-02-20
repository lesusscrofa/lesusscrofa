package be.susscrofa.api.resource;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.repository.DeliveryManRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;

public class DeliveryMansResourceTest extends AbstractResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DeliveryManRepository deliveryManRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void findDeliveryMans() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		this.deliveryManRepository.save(deliveryMan);

		this.mockMvc.perform(get("/api/deliveryMans"))
			.andDo(print())
			.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[0].firstName").value(firstName))
				.andExpect(jsonPath("$[0].lastName").value(lastName));
	}

	@Test
	void getDeliveryMan() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		deliveryMan = this.deliveryManRepository.save(deliveryMan);

		this.mockMvc.perform(get("/api/deliveryMans/{id}", deliveryMan.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("firstName").value(firstName))
				.andExpect(jsonPath("lastName").value(lastName));
	}

	@Test
	public void create() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		this.mockMvc.perform(post("/api/deliveryMans")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(deliveryMan)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(firstName))
				.andExpect(jsonPath("lastName").value(lastName));
	}

	@Test
	public void create_missingProperties() throws Exception {
		var deliveryMan = DeliveryMan.builder()
				.build();

		this.mockMvc.perform(post("/api/deliveryMans")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryMan)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors", hasItem("firstName missing")))
				.andExpect(jsonPath("$.errors", hasItem("lastName missing")));
	}

	@Test
	public void update() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		deliveryMan = deliveryManRepository.save(deliveryMan);

		var updateMan = DeliveryMan.builder()
				.firstName("Gerard2")
				.lastName("Menvuca2")
				.build();

		this.mockMvc.perform(put("/api/deliveryMans/{deliveryMansId}", deliveryMan.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateMan)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("firstName").value(updateMan.getFirstName()))
				.andExpect(jsonPath("lastName").value(updateMan.getLastName()));
	}

	@Test
	public void update_entityDontExist() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		this.mockMvc.perform(put("/api/deliveryMans/{deliveryMansId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryMan)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void update_missingParameters() throws Exception {
		var deliveryMan = DeliveryMan.builder()
				.build();

		this.mockMvc.perform(put("/api/deliveryMans/{deliveryMansId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryMan)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("firstName missing")))
				.andExpect(jsonPath("$.errors", hasItem("lastName missing")));
	}

	@Test
	public void deleteDM() throws Exception {
		var firstName = "Gerard";
		var lastName = "Menvuca";

		var deliveryMan = DeliveryMan.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		deliveryMan = deliveryManRepository.save(deliveryMan);

		this.mockMvc.perform(delete("/api/deliveryMans/{deliveryManId}", deliveryMan.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		assertThat(this.deliveryManRepository.existsById(deliveryMan.getId())).isEqualTo(false);
	}

	@Test
	public void deleteDM_notFound() throws Exception {
		this.mockMvc.perform(delete("/api/deliveryMans/{deliveryManId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
}
