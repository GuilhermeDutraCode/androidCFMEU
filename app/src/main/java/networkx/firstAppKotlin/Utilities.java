package networkx.firstAppKotlin;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
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




    //static TextView countTextView = findViewById(R.id.countTextView);
    public static void activeUserIntoExcel(File file, String tag, View view){
        try ( InputStream inp = new FileInputStream(file)){
            Workbook wb = WorkbookFactory.create(inp);
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.getSheet("Active");
            //int lastRow = -1; // get last row with content

            int lastRow = sheet.getLastRowNum(); // get last row with content
            int tagCount = countTags(sheet);
            //createContent(sheet, creationHelper, lastRow);
            createContent2(sheet, creationHelper, lastRow, tag);
            try (OutputStream fileOut = new FileOutputStream( file )) {
                wb.write(fileOut);
            }

            TextView countTextView = view.findViewById(R.id.layout);
            countTextView.setText("Total tags: " + tagCount);
        } catch (Exception e){
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



    public static void inactiveUserIntoExcel(File file,String tag){
        try ( InputStream inp = new FileInputStream(file)){
            Workbook wb = WorkbookFactory.create(inp);
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.getSheet("Inactive");
            //int lastRow = -1; // get last row with content
            int lastRow = sheet.getLastRowNum(); // get last row with content
            //createContent(sheet, creationHelper, lastRow);
            createContent2(sheet, creationHelper, lastRow, tag);
            try (OutputStream fileOut = new FileOutputStream( file )) {
                wb.write(fileOut);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void createContent2(Sheet sheet, CreationHelper creationHelper, int lastRow, String tag){
//        int coll = 1;
//        Row row = sheet.createRow(lastRow + 1);
//        for(int j = 0; j < coll; j ++){
//            Cell cell = row.createCell( j );
//            //String[] tagId = new String[0];
//            cell.setCellValue( creationHelper.createRichTextString( tag ) );
//        }

        int rowNum = sheet.getLastRowNum();

        // Check if ID already exists in the sheet
        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue != null && cellValue.equals(tag)) {
                        // ID already exists, do not create new row
                        Toast.makeText(null, "Member already added into my meeting", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }

        // ID does not exist, create new row
        Row row = sheet.createRow(rowNum + 1);
        Cell cell = row.createCell(0);
        cell.setCellValue(creationHelper.createRichTextString(tag));
    }
}

