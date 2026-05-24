import java.io.IOException;

public class Main {
	static void main() throws IOException {
		FirstFileType firstFileType = new FirstFileType();
		firstFileType.print("src/main/resources/in/discount_day.txt", "\\|", "file1.txt");
		Printer secondFile = new PrintAdapter(firstFileType);
		secondFile.print("src/main/resources/in/discount_day_without_ext", "#", "file2");
	}
}