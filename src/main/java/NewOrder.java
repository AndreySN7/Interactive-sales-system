import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrder {
	private static final int MIN_CEMENT_SALES_AMOUNT = 50;
	private static final int CEMENT_COST = 500;
	private static final int PERCENT_STEP = 5;

	private String companyName;
	private double cost;
	@Getter
	private static int discountPercent = 50;
	private Order order;

	public NewOrder(Order order) {
		this.companyName = order.getCompanyName();
		double subtotalCost = (double) order.getAmountCementPurchased() / MIN_CEMENT_SALES_AMOUNT * CEMENT_COST;
		this.cost = subtotalCost - ((double) discountPercent / 100 * subtotalCost);
		setDiscountPercent(PERCENT_STEP);
	}

	public static void setDiscountPercent(int percentStep) {
		discountPercent -= percentStep;
		if (discountPercent <= 0) {
			discountPercent = 0;
		}
	}

	@Override
	public String toString() {
		return companyName + " - " + cost;
	}
}
