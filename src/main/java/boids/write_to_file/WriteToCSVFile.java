package boids.write_to_file;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteToCSVFile {

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(";"));
    }

    public void addHeading(){
        List<String[]> heading = new ArrayList<>();
        heading.add(new String[]{"Prey average velocity","Predator average velocity"});
        try {
            this.writeToFile(heading);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToFile(List<String[]> dataLines) throws IOException {
        FileWriter fileWriter = new FileWriter("target/generated-sources/test.csv",true);
        PrintWriter pw = new PrintWriter(fileWriter);
        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        pw.close();

    }
}


