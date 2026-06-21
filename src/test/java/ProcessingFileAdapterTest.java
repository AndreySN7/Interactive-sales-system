import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessingFileAdapterTest {
	private FileProcessing fp;
	private ProcessingFileAdapter pfa;
	private String fileAdapterPath = "src/main/resources/in/test_discount_day_without_ext";
	private String processingFileAdapterFileName = "testAdapter.txt";
	private String fileAdapterPathOut = "src/main/resources/out/" + processingFileAdapterFileName;
	private String baseClassPath = "src/main/resources/in/test_discount_day.txt";

	@BeforeEach
	void setUp() {
		fp = new FileProcessing();
		pfa = new ProcessingFileAdapter(fp);
		pfa.setFileAdapterPath(fileAdapterPath);
		pfa.setProcessingFileAdapterFileName(processingFileAdapterFileName);
	}

	@AfterEach
	void tearDown() {
		try {
			Files.deleteIfExists(Path.of(fileAdapterPath));
			Files.deleteIfExists(Path.of(baseClassPath));
			Files.deleteIfExists(Path.of(fileAdapterPathOut));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@DisplayName("Файл не найден")
	void processingInFileAndSaveToFile_WhenFileNotExist_ShouldThrowException() {
		pfa.setFileAdapterPath("src/main/resources/in/super-mega-test.txt");

		assertThrows(RuntimeException.class, () -> pfa.processingInFileAndSaveToFile(),
					"Должно выйти исключение \"Error reading file\"");
	}

	@Test
	@DisplayName("Файл пустой")
	void processingInFileAndSaveToFile_WhenFileIsEmpty_ShouldThrowException() {
		List<String> orders = List.of();
		createTestFileIn(orders);

		assertThrows(RuntimeException.class, () -> pfa.processingInFileAndSaveToFile());
	}

	@Test
	@DisplayName("Корректная обработка файла")
	void processingInFileAndSaveToFile_WhenEverythingIsFine_ShouldWorkCorrectly() {
		List<String> orders = List.of(
					"2021-02-09T16:00:22#Industrial#8800",
					"2021-02-09T10:48:34#Mosque#33120"
		);
		createTestFileIn(orders);
		pfa.setBaseClassPath(baseClassPath);
		fp.setPath(baseClassPath);

		pfa.processingInFileAndSaveToFile();
		List<String> resultOutFile = readTestFileOut();
		List<String> result = List.of(
					"Mosque - 165600.0",
					"Industrial - 48400.0"
		);

		assertEquals(result, resultOutFile);
	}

	private List<String> readTestFileOut() {
		List<String> result = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of(fileAdapterPathOut))) {
			result = lines.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void createTestFileIn(List<String> list) {
		try (PrintWriter pw = new PrintWriter(fileAdapterPath)) {
			list.stream().forEach(pw::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pfa.setFileAdapterPath(fileAdapterPath);
	}
}