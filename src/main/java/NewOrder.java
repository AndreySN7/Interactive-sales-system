import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrder {
	private static final int MIN_CEMENT_SALES_AMOUNT = 50;
	private static final int CEMENT_COST = 500;

	private String companyName;
	private double cost;
	static int discountPercent = 50;
	private Order order;


	public NewOrder(Order order) {
		this.companyName = order.getCompanyName();
		double subtotalCost = (double) order.getAmountCementPurchased() / MIN_CEMENT_SALES_AMOUNT * CEMENT_COST;
		this.cost =  subtotalCost - ((double) NewOrder.discountPercent / 100 * subtotalCost);
		discountPercent -= 5;
	}

	@Override
	public String toString() {
		return "NewOrder{" +
					"companyName='" + companyName + '\'' +
					", cost=" + cost +
					'}';
	}
}
