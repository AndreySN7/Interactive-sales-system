import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessingFileAdapter extends FileProcessing implements ProcessingFile {
	private FileProcessing fileProcessing;

	public ProcessingFileAdapter(FileProcessing fileProcessing) {
		this.fileProcessing = fileProcessing;
	}

	@Override
	public void processingInFileAndSaveToFile() {
		List<String> changingTheFile = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Path.of("src/main/resources/in/discount_day_without_ext"))) {
			changingTheFile = lines
						.map(s -> s.replaceAll("#", "|"))
						.toList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (PrintWriter pw = new PrintWriter("src/main/resources/in/discount_day.txt")) {
			changingTheFile.stream()
						.forEach(pw::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileProcessing.setFileName("file2.txt");
		fileProcessing.processingInFileAndSaveToFile();
	}
}