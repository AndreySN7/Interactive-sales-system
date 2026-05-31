import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirstFileType implements Printer {

	@Override
	public void print(String path, String spliterator, String nameFileOut) {
		NewOrder.setDiscountPercent(50);
		List<NewOrder> orders = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of(path))) {
			orders = lines
						.map(line -> line.split(spliterator))
						.map(s -> {
							if (s.length < 3) {
								throw new RuntimeException("Invalid spliterator: '" + spliterator + "' or file format!");
							}
							return new Order(LocalDateTime.parse(s[0]), s[1], Integer.parseInt(s[2]));
						})
						.sorted(Comparator.comparing(Order::getOrderSubmissionTime))
//						.peek(System.out::println)
						.map(s -> new NewOrder(s))
//						.peek(System.out::println)
						.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String pathOutFile = "src/main/resources/out/" + nameFileOut;
		try (PrintWriter pw = new PrintWriter(pathOutFile)) {
			orders.stream()
						.map(NewOrder::toString)
						.forEach(pw::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
