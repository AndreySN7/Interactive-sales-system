import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileProcessingTest {
	private static String pathIn = "src/main/resources/in/test.txt";
	private static String pathOut = "src/main/resources/out/test";
	private FileProcessing fp;

	@BeforeEach
	void setup() {
		fp = new FileProcessing();
		fp.setFileName("test");
	}

	//	@AfterEach
//	void tearDown() {
//		try {
//			Files.deleteIfExists(Path.of(pathIn));
//			Files.deleteIfExists(Path.of(pathOut));
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Test
	@DisplayName("Вместо количества цемента строка")
	public void processingInFileAndSaveToFile_WhenStringInsteadOfNumberCement_ShouldThrowException() {
		List<String> orders = List.of(
					"2021-02-09T16:00:22|Industrial|8r800",
					"2021-02-09T10:48:34|Mosque|33120"
		);
		createTestFileIn(orders);

		assertThrows(InputMismatchException.class, () -> fp.processingInFileAndSaveToFile(),
					"Должно выйти исключение \"Invalid amountCementPurchased: 8r800\"");
	}

	@Test
	@DisplayName("Некорректное (отрицательное) количество цемента в файле")
	public void processingInFileAndSaveToFile_WhenIncorrectAmountCement_ShouldThrowException() {
		List<String> orders = List.of(
					"2021-02-09T16:00:22|Industrial|-8800",
					"2021-02-09T10:48:34|Mosque|33120"
		);
		createTestFileIn(orders);

		assertThrows(InputMismatchException.class, () -> fp.processingInFileAndSaveToFile(),
					"Должно выйти исключение \"Invalid amountCementPurchased: -8800\"");
	}

	@Test
	@DisplayName("Выдает исключение с другим разделителем")
	public void processingInFileAndSaveToFile_WhenOtherSpliterator_ShouldThrowException() {
		List<String> orders = List.of(
					"2021-02-09T16:00:22|Industrial?8800",
					"2021-02-09T10:48:34|Mosque|33120"
		);
		createTestFileIn(orders);

		assertThrows(RuntimeException.class, () -> fp.processingInFileAndSaveToFile(),
					"Должно выйти исключение \"Invalid spliterator: '|' or file format!\"");
	}

	@Test
	@DisplayName("Корректная обработка файла")
	public void processingInFileAndSaveToFile_WhenEverythingIsFine_ShouldWorkCorrectly() {
		List<String> orders = List.of(
					"2021-02-09T16:00:22|Industrial|8800",
					"2021-02-09T10:48:34|Mosque|33120"
		);
		createTestFileIn(orders);

		fp.processingInFileAndSaveToFile();
		List<String> resultOutFile = readTestFileOut();
		List<String> result = List.of(
					"Mosque - 165600.0",
					"Industrial - 48400.0"
		);

		Assertions.assertTrue(result.equals(resultOutFile));
	}

	private List<String> readTestFileOut() {
		List<String> result = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of(pathOut))) {
			result = lines.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void createTestFileIn(List<String> list) {
		try (PrintWriter pw = new PrintWriter(pathIn)) {
			list.stream().forEach(pw::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		fp.setPath(pathIn);
	}
}