import java.time.LocalDateTime;

public class Main {
	static void main() {
		String orderDateTime = "2021-02-09T16:00:22";
		LocalDateTime orderDate = LocalDateTime.parse(orderDateTime);
		String companyName = "Industrial";
		int amountCementPurchased = 8800;

		Order order1 = new Order(orderDate, companyName, amountCementPurchased);
		NewOrder newOrder1 = new NewOrder(order1);

		System.out.println(newOrder1);
		System.out.println(NewOrder.discountPercent);
	}
}
