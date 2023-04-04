package networkx.firstAppKotlin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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
    private EditText dateEdt;
    private EditText fileNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_meeting);

        createWorkbookButton = findViewById(R.id.saveA);
        dateEdt = findViewById(R.id.idEdtDate);
        fileNameEditText = findViewById(R.id.filename_input);

        fileNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    fileNameEditText.setText("");
                } else {
                    // Hide the keyboard when the editText loses focus
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });



        //Function to make calendar pop to user pick meeting date;
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewMeeting.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        //Function to create a new workbook;
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

        createCell(row1, 0, "d");
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
        String fileNameInput = fileNameEditText.getText().toString().trim();

        if (fileNameInput.isEmpty()) {
            Toast.makeText(this, "Please enter a file name", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText dateEdt = findViewById(R.id.idEdtDate);
        String dateStr = dateEdt.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


        try {
            Date date = sdf.parse(dateStr);
            sdf.applyPattern("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            String fileName = formattedDate + "__" + fileNameInput + ".xlsx";

            File excelFile = new File(folderPath, fileName);

            try {
                FileOutputStream fileOut = new FileOutputStream(excelFile);
                ourWorkbook.write(fileOut);
                fileOut.close();
                Toast.makeText(this, "Meeting created", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }


}