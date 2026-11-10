package com.example.inputform;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DataInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.editTextDate2);
        editText.setOnClickListener(v -> showDatePickerDialog(editText));

        Button submitBtn = findViewById(R.id.button2);
        submitBtn.setOnClickListener(v -> {
            EditText nameBox = findViewById(R.id.editTextTextPersonName2);
            EditText emailBox = findViewById(R.id.editTextTextEmailAddress2);

            String name = (nameBox != null) ? nameBox.getText().toString() : "Unknown";
            String email = emailBox.getText().toString();
            String dateOfBirth = editText.getText().toString();

            RadioGroup Rgroup = findViewById(R.id.maritalStatusGroup);
            int selectedId = Rgroup.getCheckedRadioButtonId();

            String maritalStatus = "None Selected";
            if (selectedId != -1) {
                RadioButton selectedButton = findViewById(selectedId);
                maritalStatus = selectedButton.getText().toString();
            }

            String summaryMessage = "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "DOB: " + dateOfBirth + "\n" +
                    "Status: " + maritalStatus;

            new AlertDialog.Builder(DataInput.this)
                    .setTitle("Success!")
                    .setMessage(summaryMessage)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void showDatePickerDialog(EditText dateInputField) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            int displayMonth = month + 1;
            String formattedDate = displayMonth + "/" + dayOfMonth + "/" + year;
            dateInputField.setText(formattedDate);
        };

        int startYear = 2004;
        int startMonth = 0;
        int startDay = 1;

        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, startYear, startMonth, startDay);
        dialog.show();
    }

    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //find the menu item that is selected
        int itemId = item.getItemId();
        if (itemId == R.id.itemNext) {
            displayNextAlert();
            return true;
        } else if (itemId == R.id.itemExit) {
            //do something
            //finish();
            popupToast("You want to exit why not just start another application");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void displayNextAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Next")
                .setMessage("Next clicked")
                .setPositiveButton("OK", null)
                .show();
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
