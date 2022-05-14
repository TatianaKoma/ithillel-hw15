package ua.ithillel;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static String FILE_INPUT_PATH = "tolstoy_voyna-i-mir__xoraa_436421.txt";
    public static String FILE_OUTPUT_PATH = "file.txt";

    public static void main(String[] args) {
        Map<String, Long> sortedWordsNumber = readFromFile(FILE_INPUT_PATH);
        String formattedResult = format(sortedWordsNumber);
        writeInFile(FILE_OUTPUT_PATH, formattedResult);
    }

    private static Map<String, Long> readFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String regex = "^русс.*|росс.*|расея|русь";
            return br.lines()
                    .flatMap(line -> Arrays.stream(line.split("[- ,.[***]();?!\"\\d:\\'«…»\\]\\[]")))
                    .filter(line -> !line.isBlank())
                    .filter(line -> line.length() > 1)
                    .map(String::toLowerCase)
                    .filter(s -> !s.matches(regex))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static String format(Map<String, Long> map) {
        return map.entrySet().stream()
//               .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "-" + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    private static void writeInFile(String filePath, String info) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
