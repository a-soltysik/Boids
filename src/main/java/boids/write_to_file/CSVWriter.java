package boids.write_to_file;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {

    private String fileName;
    private int bufferSize;
    private String[] headers;
    private List<String[]> dataLines = new ArrayList<>();
    private String header;
    private HashMap<String,ArrayList<Float>> values = new HashMap<>();
    private HashMap<Integer,String> index = new HashMap<>();
    private int count=0;

    public CSVWriter(String fileName, int bufferSize,String[] headers){
        this.fileName=fileName;
        this.bufferSize=bufferSize;
        this.headers=headers;
        addHeader();
        for (int i=0;i< headers.length;i++){
            values.put(headers[i],new ArrayList<Float>());
        }
    }

    public  void addToBuffer(String header, float value) {
        count++;
        values.get(header).add(value);
        if(count % (bufferSize * headers.length) == 0){
            saveFromBuffer(values,index);
            resetValues();
        }

    }

    public void setIndex(String header){
        for (int i = 0; i<headers.length ; i++){
            if(headers[i].equals(header)){
                index.put(i,header);
            }
        }
    }

    private void saveFromBuffer(HashMap<String,ArrayList<Float>> values,HashMap<Integer,String> index){
        for(int i=0;i<bufferSize;i++) {
            String[] writeable = new String[headers.length];
            for(int j=0; j< headers.length;j++){
                writeable[j] = values.get(index.get(j)).get(i).toString();
            }
            dataLines.add(writeable);
        }
            try {
                writeToFile(dataLines, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void resetValues(){
        values.replaceAll((k, v) -> new ArrayList<Float>());
        dataLines.clear();
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(";"));
    }

    private void addHeader(){
        List<String[]> headers = new ArrayList<>();
        headers.add(this.headers);
        try {
            this.writeToFile(headers,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(List<String[]> dataLines,String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName,true);
        PrintWriter pw = new PrintWriter(fileWriter);
        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        pw.close();

    }
}


