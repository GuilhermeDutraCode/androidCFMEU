package networkx.firstAppKotlin;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utilities {


    public static List<String> getAllFiles(String URI){
        List<String> fileNames = new ArrayList<>();
        try {
            Path folder = Paths.get(URI, "CFMEU_Meetings/");
            Arrays.asList(folder.toFile().listFiles()).forEach(file -> {
                String fileName = file.getName();
                String formattedName = getFormattedName(fileName);
//                String formattedName = fileName;
                fileNames.add(formattedName);
              // getFormattedName(fileName);
            });
        } catch (Exception e) {
            Log.d("file-jav-err", e.getMessage());
        }

        return fileNames;
    }


//add this to the list page!
    protected static String getFormattedName(String fileName) {
        String[] parts = fileName.split("__");
        if (parts.length == 2) {
            String date = parts[0];
            String nameWithExtension = parts[1];
            String[] nameParts = nameWithExtension.split("\\.");
            String name = nameParts[0];
            return name + " " + date;
        } else {
            return fileName;
        }
    }

    //Pass this as intent to scan and select all meetings:
    protected static  String putFormattedName(String fileName) {
        String[] parts = fileName.split(" ");
        if (parts.length == 2) {
            String date = parts[1];
            String nameWithExtension = parts[0];
            String[] nameParts = nameWithExtension.split("\\.");
            String name = nameParts[0];
            return date + "__" + name + ".xlsx";

        } else {
            return fileName;
        }
    }

         public static void  storageLocation(View view, Context context) {
            File ourFileDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            String S1 =  ourFileDirectory.toString();
            S1 = S1.replace("/storage/emulated/0", "Internal Storage");
            S1 = S1.replace("/", " > ");
            S1 = S1 + " > CFMEU_Meeting";



            TextView sPath = view.findViewById(R.id.sPath);
            sPath.setText(S1);

        }

        public static void deletedMeetingsPath(View view, Context context){
            File ourFileDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            String S1 =  ourFileDirectory.toString();
            S1 = S1.replace("/storage/emulated/0", "Internal Storage");
            S1 = S1.replace("/", " > ");
            S1 = S1 + " > Deleted_Meetings";

            TextView dPath = view.findViewById(R.id.dPath);
            dPath.setText(S1);
        }

    public static void tagsLocation(View view, Context context){
        File ourFileDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String S1 =  ourFileDirectory.toString();
        S1 = S1.replace("/storage/emulated/0", "Internal Storage");
        S1 = S1.replace("/", " > ");


        TextView tPath = view.findViewById(R.id.tPath);
        tPath.setText(S1);
    }



    //static TextView countTextView = findViewById(R.id.countTextView);
    public static void activeUserIntoExcel(File file, String tag, String wSheet, TextView countTextView, TextView alreadyInTextView){
        try (InputStream inp = new FileInputStream(file)) {
            Workbook wb = WorkbookFactory.create(inp);
            CreationHelper creationHelper = wb.getCreationHelper();

            Sheet sheet = wb.getSheet(wSheet);
            //  int lastRow = sheet.getLastRowNum(); //ultima cell com any data
            //countTextView.setText("1");
            createContent2(sheet, creationHelper, tag, alreadyInTextView, 0);
            try (OutputStream fileOut = new FileOutputStream(file)) {
                wb.write(fileOut);
            }
            if (wSheet == "Active"){
                int lastRow = sheet.getLastRowNum() == 0 ? 1 : sheet.getLastRowNum();
            // createContent2(sheet, creationHelper, lastRow, tag, alreadyInTextView);
            countTextView.setText(String.valueOf(lastRow));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int countTags(Sheet sheet) {
        int tagCount = 0;
        int rowNum = sheet.getLastRowNum();

        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue != null && !cellValue.isEmpty()) {
                        tagCount++;
                    }
                }
            }
        }

        return tagCount;
    }



    public static void inactiveUserIntoExcel(File file,String tag, TextView alreadyInTextView){
        try ( InputStream inp = new FileInputStream(file)){
            Workbook wb = WorkbookFactory.create(inp);
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.getSheet("Inactive");
            //int lastRow = -1; // get last row with content
            int lastRow = sheet.getLastRowNum(); // get last row with content
            //createContent(sheet, creationHelper, lastRow);
            createContent2(sheet, creationHelper,  tag, alreadyInTextView,  lastRow);
            try (OutputStream fileOut = new FileOutputStream( file )) {
                wb.write(fileOut);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void createContent2(Sheet sheet, CreationHelper creationHelper, String tag, TextView alreadyInTextView, int lastRow){
        int rowNum = sheet.getLastRowNum();
        alreadyInTextView.setText("");
        // Check if ID already exists in the sheet
        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue != null && cellValue.equals(tag)) {
                        // ID already exists, do not create new row
                        alreadyInTextView.setText("Member presence already added to meeting");
                        alreadyInTextView.setTextColor(Color.RED);
                        return;
                    }
                }
            }
        }

        Row row = sheet.createRow(rowNum + 1);
        Cell cell = row.createCell(0);
        cell.setCellValue(creationHelper.createRichTextString(tag));;

        CellStyle timeStyle = sheet.getWorkbook().createCellStyle();
        timeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("h:mm:ss a"));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(new Date());
        cell2.setCellStyle(timeStyle);
    }
}

