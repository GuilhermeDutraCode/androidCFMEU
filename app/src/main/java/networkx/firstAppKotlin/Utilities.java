package networkx.firstAppKotlin;

import android.util.Log;

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

//    public static void createFilesToTest(String path) throws IOException {
//        for (int i  = 0; i< 10; i ++){
//            File file = new File(path, "CFMEU_Meetings/" + UUID.randomUUID().toString().concat(".txt"));
//            file.createNewFile();
//        }
//    }

    public static void creteFileWithPermissions(File file){
        try {
            FileOutputStream f = new FileOutputStream(file);
            String hello = "Hello world";
            for(int i = 0; i < hello.toCharArray().length; i ++){
                f.write( hello.toCharArray()[i] );
            }
            f.close();
        } catch (FileNotFoundException e) {

             throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
