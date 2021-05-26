package boids;


import java.io.*;
import java.util.*;

public class CSVWriter {

    public final String fileName;
    private final PrintWriter printWriter;
    private final int bufferSize;
    private final String[] headers;
    private final List<String[]> dataLines = new ArrayList<>();
    private final HashMap<String,ArrayList<Float>> values = new HashMap<>();
    private final HashMap<Integer,String> indices = new HashMap<>();
    private int count=0;

    public CSVWriter(String fileName, int bufferSize,String[] headers) throws IOException {
        this.fileName=fileName;
        this.bufferSize=bufferSize;
        this.headers=headers;
        FileWriter fileWriter = new FileWriter(fileName, false);
        printWriter = new PrintWriter(fileWriter);
        writeHeaders();
        for (String header : headers) {
            values.put(header, new ArrayList<>());
            setIndices(header);
        }
    }

    public void close() {
        printWriter.close();
    }

    public void addToBuffer(String header, float value) {
        count++;
        values.get(header).add(value);
        if(count % (bufferSize * headers.length) == 0){
            saveFromBuffer();
            resetValues();
        }
    }

    private void setIndices(String header){
        for (int i = 0; i<headers.length ; i++){
            if(headers[i].equals(header)){
                indices.put(i,header);
            }
        }
    }

    private void saveFromBuffer() {
        for(int i=0;i<bufferSize;i++) {
            String[] writeable = new String[headers.length];
            for(int j=0; j< headers.length;j++){
                writeable[j] = values.get(indices.get(j)).get(i).toString();
            }
            dataLines.add(writeable);
        }
        writeToFile(dataLines);
    }

    private void resetValues(){
        values.replaceAll((k, v) -> new ArrayList<>());
        dataLines.clear();
    }

    private void writeHeaders(){
        writeToFile(new ArrayList<>(Collections.singletonList(headers)));
    }

    private void writeToFile(List<String[]> dataLines) {
        dataLines.stream()
                .map(o -> String.join(";", o))
                .forEach(printWriter::println);
    }
}


