import java.io.*;

public class FileManager {
    private static FileManager manager = new FileManager();

    public static FileManager getInstance() {
        return manager;
    }

    public boolean writeToFile(String path, String text) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "utf-8"))) {
            writer.write(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String readFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
