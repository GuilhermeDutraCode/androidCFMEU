package networkx.firstAppKotlin;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
}
