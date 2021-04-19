package boids.write_to_file;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {
    private String fileName;
    private int bufferSize;
    private String header1 = "Prey average velocity";
    private String header2 = "Predator average velocity";
    public CSVWriter(String fileName, int bufferSize){
        this.fileName=fileName;
        this.bufferSize=bufferSize;
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(";"));
    }

    public void addHeader(){
        List<String[]> heading = new ArrayList<>();
        heading.add(new String[]{header1,header2});
        try {
            this.writeToFile(heading,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(List<String[]> dataLines,String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName,true);
        PrintWriter pw = new PrintWriter(fileWriter);
        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        pw.close();

    }
}


