/**
 * @author Balakrishna Lellapalli
 *
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOperation {

	//Read instruction from "input" file line by line (currentFilePointer is the line number to be read)
	public static String getContent(int currentFilePointer) throws IOException {
		String instr = "";
		try {
			Stream<String> lines = Files.lines(Paths.get("Input"));
			instr = lines.skip(currentFilePointer).findFirst().get();
		} catch (Exception ex) {
		}
		return instr;
	}
}