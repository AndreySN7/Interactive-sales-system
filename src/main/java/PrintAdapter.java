import java.io.IOException;

public class PrintAdapter implements Printer {
	private FirstFileType firstFileType;

	public PrintAdapter(FirstFileType firstFileType) {
		this.firstFileType = firstFileType;
	}

	@Override
	public void print (String path, String spliterator, String nameFileOut) throws IOException {
		firstFileType.print(path, spliterator, nameFileOut);
	}
}
