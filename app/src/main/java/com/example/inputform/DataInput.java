package com.example.inputform;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DataInput extends AppCompatActivity {
    private String[] status = {"full-time", "part-time", "student", "other"};

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

        DatePicker dp = findViewById(R.id.DatePickerDOB);
        dp.init(2004, 1, 13, null);

        Button btnNext = findViewById(R.id.ButtonNext);
        btnNext.setOnClickListener(v -> displayNextAlert());

        Spinner sp = findViewById(R.id.spinner); // reference to the spinner

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    private void displayNextAlert() {
        CheckBox agreeTerms = findViewById(R.id.chkAgreeTerms);

        if (!agreeTerms.isChecked()) {
            popupToast("Sorry, you need to agreed with the teams even " +
                    "though you don't know what they are");
            return;
        }

        // Get what the user entered
        EditText nameInput = findViewById(R.id.editTextName);
        DatePicker dobInput = findViewById(R.id.DatePickerDOB);
        EditText emailInput = findViewById(R.id.editTextEmail);
        Spinner sp = findViewById(R.id.spinner); // getting the reference

        // Get the text entered in the EditText using getText()
        String strName = nameInput.getText().toString();
        String strDOB = dobInput.getDayOfMonth() + "/" + (dobInput.getMonth() + 1) +
                "/" + dobInput.getYear();
        String strEmail = emailInput.getText().toString();

        int row = sp.getSelectedItemPosition(); // getting the index of the selected item
        String strStatus = status[row]; // getting the selected item

        new AlertDialog.Builder(this)
                .setTitle("Details Entered")
                .setMessage("Details entered\n"
                        + strName + "\n" + strDOB + "\n" + strEmail + "\n" + strStatus)
                .setNeutralButton("Back", (dialog, which) -> {
                    // do nothing - it will just close when clicked
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // find the menu item that is selected
        int id = item.getItemId();
        if (id == R.id.itemNext) {
            displayNextAlert();
            return true;
        } else if (id == R.id.itemExit) {
            popupToast("You want to exit why not just start another application");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
