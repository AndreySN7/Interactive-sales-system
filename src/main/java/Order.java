import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Order {
	private LocalDateTime orderSubmissionTime;
	private String companyName;
	private int amountCementPurchased; // в килограммах

	public Order(LocalDateTime orderSubmissionTime, String companyName, int amountCementPurchased) {
		this.orderSubmissionTime = orderSubmissionTime;
		this.companyName = companyName;
		this.amountCementPurchased = amountCementPurchased;
	}

	@Override
	public String toString() {
		return orderSubmissionTime + ", " + companyName + ", " + amountCementPurchased;
	}
}
