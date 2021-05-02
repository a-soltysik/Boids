package boids;


import java.io.*;
import java.util.*;

public class CSVWriter {

    private String fileName;
    private int bufferSize;
    private String[] headers;
    private List<String[]> dataLines = new ArrayList<>();
    private HashMap<String,ArrayList<Float>> values = new HashMap<>();
    private HashMap<Integer,String> indices = new HashMap<>();
    private int count=0;

    public CSVWriter(String fileName, int bufferSize,String[] headers){
        this.fileName=fileName;
        this.bufferSize=bufferSize;
        this.headers=headers;
        addHeader();
        for (String header : headers) {
            values.put(header, new ArrayList<>());
        }
    }

    public  void addToBuffer(String header, float value) {
        count++;
        values.get(header).add(value);
        if(count % (bufferSize * headers.length) == 0){
            saveFromBuffer();
            resetValues();
        }

    }

    public void setIndices(String header){
        for (int i = 0; i<headers.length ; i++){
            if(headers[i].equals(header)){
                indices.put(i,header);
            }
        }
    }

    private void saveFromBuffer(){
        for(int i=0;i<bufferSize;i++) {
            String[] writeable = new String[headers.length];
            for(int j=0; j< headers.length;j++){
                writeable[j] = values.get(indices.get(j)).get(i).toString();
            }
            dataLines.add(writeable);
        }
            try {
                writeToFile(dataLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void resetValues(){
        values.replaceAll((k, v) -> new ArrayList<>());
        dataLines.clear();
    }

    private String convertToCSV(String[] data) {
        return String.join(";", data);
    }

    private void addHeader(){
        List<String[]> headers = new ArrayList<>();
        headers.add(this.headers);
        try {
            this.writeToFile(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(List<String[]> dataLines) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName,true);
        PrintWriter pw = new PrintWriter(fileWriter);
        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        pw.close();

    }
}


