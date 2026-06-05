import java.io.IOException;

public class Main {
	static void main() {
		FileProcessing firstFile = new FileProcessing();
		firstFile.processingInFileAndSaveToFile();
		ProcessingFile secondFile = new ProcessingFileAdapter(firstFile);
		secondFile.processingInFileAndSaveToFile();
	}
}