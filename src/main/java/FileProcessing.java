import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessing {
	private static final int MIN_CEMENT_SALES_AMOUNT = 50;
	private static final int CEMENT_COST = 500;
	private static final int PERCENT_STEP = 5;

	@Setter
	private String fileName = "file.txt";
	@Setter
	private int discountPercent = 50;
	@Setter
	private String path = "src/main/resources/in/discount_day.txt";

	public void processingInFileAndSaveToFile() {
		String spliterator = "\\|";
		setDiscountPercent(50);
		List<String> orders = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of(path))) {
			if (Files.size(Path.of(path)) == 0) {
				throw new RuntimeException("File is empty");
			}
			orders = lines
						.map(line -> line.split(spliterator))
						.map(s -> {
							if (s.length < 3) {
								throw new RuntimeException("Invalid spliterator: '" + spliterator + "' or file format!");
							}
							LocalDateTime orderSubmissionTime;
							try {
								orderSubmissionTime = LocalDateTime.parse(s[0]);
							} catch (DateTimeParseException e) {
								throw new InputMismatchException("Invalid date: " + s[0]);
							}
							if (!NumberUtils.isParsable(s[2]) || Integer.parseInt(s[2]) < 0) {
								throw new InputMismatchException("Invalid amountCementPurchased: " + s[2]);
							}
							return new Order(orderSubmissionTime, s[1], Integer.parseInt(s[2]));
						})
						.sorted(Comparator.comparing(Order::getOrderSubmissionTime))
						.map(order -> {
							double subtotalCost = (double) order.getAmountCementPurchased() / MIN_CEMENT_SALES_AMOUNT * CEMENT_COST;
							double cost = subtotalCost - ((double) discountPercent / 100 * subtotalCost);
							reducingPercentByStep(PERCENT_STEP);
							return order.getCompanyName() + " - " + cost;
						})
						.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("File not found");
		}

		String pathOutFile = "src/main/resources/out/" + fileName;
		try (PrintWriter pw = new PrintWriter(pathOutFile)) {
			orders.stream()
						.forEach(pw::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reducingPercentByStep(int percentStep) {
		discountPercent -= percentStep;
		if (discountPercent <= 0) {
			discountPercent = 0;
		}
	}
}