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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    private TextView meetingName;

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

        Sheet sheet = ourWorkbook.createSheet("Active");
        ourWorkbook.createSheet("Inactive");
        addData(sheet);

        return ourWorkbook;
    }

    private void addData(Sheet sheet) {
        int numCols = 2; // Number of columns
        int numRows = 400; // Number of rows

        // Create empty rows and cells
        for (int i = 0; i < numRows; i++) {
            //Row row = sheet.createRow(i);
            for (int j = 0; j < numCols; j++) {
                //createCell(row, j, "");
            }
        }
    }

    private void createCell(@NonNull Row sheetRow, int columnIndex, String cellValue) {
        Cell ourCell = sheetRow.createCell(columnIndex);
        ourCell.setCellValue(cellValue);
    }

    private void createExcelFile(Workbook ourWorkbook) {
       // File ourFileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File ourFileDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
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

        TextView meetingName = findViewById(R.id.meetingName);

        try {
            Date date = sdf.parse(dateStr);
            sdf.applyPattern("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            String fileName = formattedDate + "__" + fileNameInput + ".xlsx";
            String displayName = fileNameInput + "  " + formattedDate;

            File excelFile = new File(folderPath, fileName);

            if(excelFile.exists()){
               // Toast.makeText(this, "Already have a meeting with this name", Toast.LENGTH_SHORT).show();
                String errorMes = "Error: Meeting with this name already exists.";
                SpannableString spannableString = new SpannableString(errorMes);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan, 0, errorMes.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                meetingName.setText(spannableString);
            } else {
                try {
                    FileOutputStream fileOut = new FileOutputStream(excelFile);
                    ourWorkbook.write(fileOut);
                    fileOut.close();
                   // Toast.makeText(this, "Meeting created", Toast.LENGTH_SHORT).show();
                    fileNameEditText.setText("");
                    dateEdt.setText("");
                    meetingName.setText(displayName);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }


}