package in.kamalaenterprize.database;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.commonutility.ShowCustomView;

public class CSVFile {
    private InputStream inputStream;
    private Context svContext;

    public CSVFile(InputStream inputStream, Context context) {
        this.inputStream = inputStream;
        this.svContext = context;
    }

    public List<String[]> read() {
        List<String[]> resultList = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            int count = 0;
            while ((csvLine = reader.readLine()) != null) {
                count++;
                if (count <= 2) {
//                    String[] row = csvLine.split(",");
//                    resultList.add(row);
                } else {
                    String[] row = csvLine.split(",");
                    resultList.add(row);
                }

            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

    private void exportDB(String DB_NAME) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + svContext.getPackageName() + "/databases/" + DB_NAME;
        String backupDBPath = DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            ShowCustomView.showCustomToast("DB Exported", svContext, ShowCustomView.ToastyInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    private String folderName 		= "foldername";
//    private String filaName 		= "filename";
//    private String filaExtension 	= ".csv";
//    private File   file;
    public void createFile(String folderName, String filaName, String filaExtension, File file) {
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), folderName);
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            try {
                file = new File(newFolder, filaName + filaExtension);
                file.createNewFile();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateCsvFile(File fileName, String colOne, String colTwo) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.append(colOne);
            writer.append(',');
            writer.append(colTwo);
            writer.append('\n');

            //generate whatever data you want
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
