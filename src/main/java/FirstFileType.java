import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FirstFileType implements Printer{

	@Override
	public void print (String path, String spliterator, String nameFileOut) throws IOException {
		File fileIn = new File(path);
		String pathOutFile = "src/main/resources/out/" + nameFileOut;
		File fileOut = new File(pathOutFile);

		PrintWriter pwFile = new PrintWriter(fileOut);

		Scanner scanner = new Scanner(fileIn);

		NewOrder.setDiscountPercent(50);
		while(scanner.hasNextLine()){
			String[] arr = scanner.nextLine().split(spliterator);
			String orderDateTime = arr[0];
			LocalDateTime orderDate = LocalDateTime.parse(orderDateTime);
			String companyName = arr[1];
			int amountCementPurchased = Integer.parseInt(arr[2]);

			Order order = new Order(orderDate, companyName, amountCementPurchased);
			NewOrder newOrder = new NewOrder(order);
			pwFile.println(newOrder);
		}

		scanner.close();
		pwFile.close();
	}
}
