package networkx.firstAppKotlin;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Utilities {
    public static List<String> getAllFiles(String URI){
        List<String> fileNames = new ArrayList<>();
        try {
            Path folder = Paths.get(URI, "CFMEU_Meetings/");
            Arrays.asList( folder.toFile().listFiles() ).forEach( file ->{
                fileNames.add(file.getName().toString());
                Log.d("File", "File=====>>>>" + file.getName().toString());
            });

        }catch (Exception e){
            Log.d("file-jav-err", e.getMessage());
        }
        return fileNames;
    }


    public static Uri getImageUri(Context context, File file) {
        Uri uri = null;
        try{
             uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        }catch (Exception e){
            e.printStackTrace();
        }


        return uri;
    }

    public static  void getFileXlsx(File folder){
        File file = new File(folder, "teste.xlsx");
        try {
            OPCPackage opcPackage = OPCPackage.open( file );
            XSSFWorkbook wb = new XSSFWorkbook( opcPackage );

            Sheet sheet = wb.createSheet();

            Row row = sheet.createRow(2);
            row.setHeightInPoints(30);

            opcPackage.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

}
