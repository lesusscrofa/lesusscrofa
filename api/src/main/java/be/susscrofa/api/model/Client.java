package be.susscrofa.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "firstName missing")
	@Size(min = 1)
	private String firstName;

	@NotNull(message = "lastName missing")
	@Size(min = 1)
	private String lastName;

	private String deliveryStreet;

	@NotNull(message = "delivery zone missing")
	private Long deliveryZoneId;
	private Integer deliveryZipCode;
	private String deliveryCity;
	private String deliveryPhone;

	private String billingStreet;
	private Integer billingZipCode;
	private String billingCity;
	private String billingPhone;

	private Integer reduction;

	private Integer deliveryPosition;

	private boolean deliveryPreferenceTakeAway;
}
