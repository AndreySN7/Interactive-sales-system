import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessingFileAdapter extends FileProcessing implements ProcessingFile {
	private FileProcessing fileProcessing;
	@Setter
	private String fileAdapterPath = "src/main/resources/in/discount_day_without_ext1";
	@Setter
	private String baseClassPath = "src/main/resources/in/discount_day.txt";
	@Setter
	private String processingFileAdapterFileName = "file2.txt";

	public ProcessingFileAdapter(FileProcessing fileProcessing) {
		this.fileProcessing = fileProcessing;
	}

	@Override
	public void processingInFileAndSaveToFile() {
		List<String> changingTheFile = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of(fileAdapterPath))) {
			if (Files.size(Path.of(fileAdapterPath)) == 0) {
				throw new RuntimeException("FileAdapter is empty");
			}
			changingTheFile = lines
						.map(s -> s.replaceAll("#", "|"))
						.toList();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file: " + e.getMessage());
		}

		try (PrintWriter pw = new PrintWriter(baseClassPath)) {
			changingTheFile.stream()
						.forEach(pw::println);
		} catch (IOException e) {
			throw new RuntimeException("Error writing file" + e.getMessage());
		}
		fileProcessing.setFileName(processingFileAdapterFileName);
		fileProcessing.processingInFileAndSaveToFile();
	}
}