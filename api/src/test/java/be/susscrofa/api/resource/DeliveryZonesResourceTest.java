package be.susscrofa.api.resource;

import be.susscrofa.api.model.DeliveryMan;
import be.susscrofa.api.model.DeliveryZone;
import be.susscrofa.api.repository.DeliveryManRepository;
import be.susscrofa.api.repository.DeliveryZoneRepository;
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

public class DeliveryZonesResourceTest extends AbstractResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;

	@Autowired
	private DeliveryManRepository deliveryManRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void findDeliveryZones() throws Exception {
		var name = "boucle 1";
		var deliveryMan = createAndSaveDeliveryMan();

		deliveryZoneRepository.save(DeliveryZone.builder()
				.name(name)
				.deliveryManId(deliveryMan.getId())
				.build());
		
		this.mockMvc.perform(get("/api/deliveryZones"))
			.andDo(print())
			.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[0].name").value(name))
				.andExpect(jsonPath("$[0].deliveryManId").value(deliveryMan.getId()));
	}

	@Test
	void getDeliveryZone() throws Exception {
		var name = "Boucle 1";
		var deliveryMan = createAndSaveDeliveryMan();

		var deliveryZone = deliveryZoneRepository.save(DeliveryZone.builder()
				.name(name)
				.deliveryManId(deliveryMan.getId())
				.build());

		this.mockMvc.perform(get("/api/deliveryZones/{id}", deliveryZone.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("name").value(name))
				.andExpect(jsonPath("deliveryManId").value(deliveryMan.getId()));
	}

	@Test
	public void create() throws Exception {
		var name = "boucle1";
		var deliveryMan = createAndSaveDeliveryMan();

		var deliveryZone = DeliveryZone.builder()
				.name(name)
				.deliveryManId(deliveryMan.getId())
				.build();

		this.mockMvc.perform(post("/api/deliveryZones")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(deliveryZone)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("name").value(name))
				.andExpect(jsonPath("deliveryManId").value(deliveryMan.getId()));
	}

	@Test
	public void create_missingProperties() throws Exception {

		var deliveryZone = DeliveryZone.builder()
				.build();

		this.mockMvc.perform(post("/api/deliveryZones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryZone)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("errors", hasItem("name missing")));
	}

	@Test
	public void update() throws Exception {
		var deliveryMan = createAndSaveDeliveryMan();

		var deliveryZone = DeliveryZone.builder()
				.name("boucle1")
				.deliveryManId(deliveryMan.getId())
				.build();

		deliveryZone = deliveryZoneRepository.save(deliveryZone);

		var updateZone = DeliveryZone.builder()
				.name("boucle1")
				.deliveryManId(deliveryMan.getId())
				.build();

		this.mockMvc.perform(put("/api/deliveryZones/{deliveryZonesId}", deliveryZone.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateZone)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("name").value(updateZone.getName()))
				.andExpect(jsonPath("deliveryManId").value(updateZone.getDeliveryManId()));
	}

	@Test
	public void update_entityDontExist() throws Exception {
		var deliveryMan = createAndSaveDeliveryMan();

		var deliveryZone = DeliveryZone.builder()
				.name("boucle1")
				.deliveryManId(deliveryMan.getId())
				.build();

		this.mockMvc.perform(put("/api/deliveryZones/{deliveryZonesId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryZone)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void update_missingParameters() throws Exception {
		var deliveryZone = DeliveryZone.builder()
				.build();

		this.mockMvc.perform(put("/api/deliveryZones/{deliveryZonesId}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deliveryZone)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("name missing")));
	}

	@Test
	public void deleteDZ() throws Exception {
		var deliveryMan = createAndSaveDeliveryMan();

		var deliveryZone = DeliveryZone.builder()
				.name("boucle1")
				.deliveryManId(deliveryMan.getId())
				.build();

		deliveryZone = deliveryZoneRepository.save(deliveryZone);

		this.mockMvc.perform(delete("/api/deliveryZones/{deliveryZoneId}", deliveryZone.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		assertThat(deliveryZoneRepository.existsById(deliveryZone.getId())).isEqualTo(false);
	}

	@Test
	public void deleteDZ_notFound() throws Exception {
		this.mockMvc.perform(delete("/api/deliveryZones/{deliveryZoneId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	private DeliveryMan createAndSaveDeliveryMan() {
		return this.deliveryManRepository.save(DeliveryMan
				.builder()
				.firstName("first")
				.lastName("last")
				.build());
	}
}
