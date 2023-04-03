package networkx.firstAppKotlin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class NewMeeting extends AppCompatActivity {

    private Button createWorkbookButton;
    private File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_meeting);

        createWorkbookButton = findViewById(R.id.saveA);
        createWorkbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workbook workbook = createWorkbook();
                createExcelFile(workbook);
            }
        });
    }

    private Workbook createWorkbook() {
        Workbook ourWorkbook = new XSSFWorkbook();

        Sheet sheet = ourWorkbook.createSheet("statSheet");
        ourWorkbook.createSheet("testSheet");
        addData(sheet);

        return ourWorkbook;
    }

    private void addData(Sheet sheet) {
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        Row row3 = sheet.createRow(2);
        Row row4 = sheet.createRow(3);
        Row row5 = sheet.createRow(4);
        Row row6 = sheet.createRow(5);
        Row row7 = sheet.createRow(6);
        Row row8 = sheet.createRow(7);

        createCell(row1, 0, "Name");
        createCell(row1, 1, "Score");

        createCell(row2, 0, "Mike");
        createCell(row2, 1, "470");

        createCell(row3, 0, "Montessori");
        createCell(row3, 1, "460");

        createCell(row4, 0, "Sandra");
        createCell(row4, 1, "380");

        createCell(row5, 0, "Moringa");
        createCell(row5, 1, "300");

        createCell(row6, 0, "Torres");
        createCell(row6, 1, "270");

        createCell(row7, 0, "McGee");
        createCell(row7, 1, "420");

        createCell(row8, 0, "Gibbs");
        createCell(row8, 1, "510");
    }

    private void createCell(Row sheetRow, int columnIndex, String cellValue) {
        Cell ourCell = sheetRow.createCell(columnIndex);
        ourCell.setCellValue(cellValue);
    }

    private void createExcelFile(Workbook ourWorkbook) {
        File ourFileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String folderName = "CFMEU_Meetings";
        String folderPath = ourFileDirectory + "/" + folderName;

        if (ourFileDirectory != null && !ourFileDirectory.exists()) {
            ourFileDirectory.mkdirs();
        }

        EditText fileNameEditText = findViewById(R.id.filename_input);
        String fileName = fileNameEditText.getText().toString().trim() + ".xlsx";

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Please enter a file name", Toast.LENGTH_SHORT).show();
            return;
        }

        File excelFile = new File(folderPath, fileName);

        try {
            FileOutputStream fileOut = new FileOutputStream(excelFile);
            ourWorkbook.write(fileOut);
            fileOut.close();
            Toast.makeText(this, "meeting created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
        }
    }
}
