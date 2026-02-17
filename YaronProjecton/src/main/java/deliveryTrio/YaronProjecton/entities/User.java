package deliveryTrio.YaronProjecton.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class User {
	private String username;
	@ToString.Exclude
	private String password;
}
