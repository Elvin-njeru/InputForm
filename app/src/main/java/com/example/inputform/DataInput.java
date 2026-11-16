package com.example.inputform;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class DataInput extends AppCompatActivity {
    private String[] status = {"full-time", "part-time", "student", "other"};

    private static final String KEY_NAME = "key_name";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_YEAR = "key_year";
    private static final String KEY_MONTH = "key_month";
    private static final String KEY_DAY = "key_day";
    private static final String KEY_SPINNER_POS = "key_spinner_pos";
    private static final String KEY_COMMENTS = "key_comments";
    private static final String KEY_TERMS = "key_terms";

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
        if (savedInstanceState != null) {
            int y = savedInstanceState.getInt(KEY_YEAR, 2004);
            int m = savedInstanceState.getInt(KEY_MONTH, 1);
            int d = savedInstanceState.getInt(KEY_DAY, 13);
            dp.init(y, m, d, null);
        } else {
            dp.init(2004, 1, 13, null);
        }

        Button btnNext = findViewById(R.id.ButtonNext);
        btnNext.setOnClickListener(v -> handleSubmitAndConfirm());

        Spinner sp = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        if (savedInstanceState != null) {
            sp.setSelection(savedInstanceState.getInt(KEY_SPINNER_POS, 0));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText nameInput = findViewById(R.id.editTextName);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText commentsInput = findViewById(R.id.editTextComments);
        DatePicker dobInput = findViewById(R.id.DatePickerDOB);
        Spinner sp = findViewById(R.id.spinner);
        CheckBox agreeTerms = findViewById(R.id.chkAgreeTerms);

        outState.putString(KEY_NAME, nameInput.getText().toString());
        outState.putString(KEY_EMAIL, emailInput.getText().toString());
        outState.putString(KEY_COMMENTS, commentsInput.getText().toString());
        outState.putInt(KEY_YEAR, dobInput.getYear());
        outState.putInt(KEY_MONTH, dobInput.getMonth());
        outState.putInt(KEY_DAY, dobInput.getDayOfMonth());
        outState.putInt(KEY_SPINNER_POS, sp.getSelectedItemPosition());
        outState.putBoolean(KEY_TERMS, agreeTerms.isChecked());
    }

    private void handleSubmitAndConfirm() {
        EditText nameInput = findViewById(R.id.editTextName);
        EditText emailInput = findViewById(R.id.editTextEmail);
        DatePicker dobInput = findViewById(R.id.DatePickerDOB);
        Spinner sp = findViewById(R.id.spinner);
        EditText commentsInput = findViewById(R.id.editTextComments);
        CheckBox agreeTerms = findViewById(R.id.chkAgreeTerms);

        // 1. Validation: Name (required, non-empty)
        String strName = nameInput.getText().toString().trim();
        if (strName.isEmpty()) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            popupToast("Validation failed: Please enter a name");
            return;
        }

        // 2. Validation: Email (required, valid email pattern)
        String strEmail = emailInput.getText().toString().trim();
        if (strEmail.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            popupToast("Validation failed: Please enter an email address");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            emailInput.setError("Enter a valid email address (e.g. name@domain.com)");
            emailInput.requestFocus();
            popupToast("Validation failed: Invalid email format");
            return;
        }

        // 3. Validation: Date of Birth (must not be in the future)
        Calendar chosenDate = Calendar.getInstance();
        chosenDate.set(dobInput.getYear(), dobInput.getMonth(), dobInput.getDayOfMonth());
        Calendar today = Calendar.getInstance();
        if (chosenDate.after(today)) {
            popupToast("Validation failed: Date of birth cannot be in the future");
            return;
        }

        // 4. Validation: Mandatory Terms Checkbox
        if (!agreeTerms.isChecked()) {
            popupToast("Validation failed: Please agree with the terms and conditions");
            return;
        }

        // Single-choice and Optional field extraction
        int row = sp.getSelectedItemPosition();
        String strStatus = (row >= 0 && row < status.length) ? status[row] : "Unknown";
        String strDOB = dobInput.getDayOfMonth() + "/" + (dobInput.getMonth() + 1) + "/" + dobInput.getYear();
        String strComments = commentsInput.getText().toString().trim();
        if (strComments.isEmpty()) {
            strComments = "(None provided)";
        }

        String readableSummary = "FORM SUMMARY\n"
                + "------------------------\n"
                + "Name: " + strName + "\n"
                + "Email: " + strEmail + "\n"
                + "DOB: " + strDOB + "\n"
                + "Work Status: " + strStatus + "\n"
                + "Comments: " + strComments + "\n"
                + "Terms Agreed: Yes";

        // Show Confirmation AlertDialog with Confirm and Edit/Cancel behaviour
        new AlertDialog.Builder(this)
                .setTitle("Confirm Submission")
                .setMessage(readableSummary)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Committing record: persist / navigate to view screen
                    commitRecord(readableSummary);
                })
                .setNegativeButton("Edit / Cancel", (dialog, which) -> {
                    // Retain entered values, do not commit, inform user
                    popupToast("Submission cancelled. Values retained for editing.");
                })
                .show();
    }

    private void commitRecord(String summaryText) {
        popupToast("Record successfully committed!");
        navigateToDisplayActivity(summaryText);
    }

    private void navigateToDisplayActivity(String content) {
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra(DisplayActivity.EXTRA_DISPLAY_TEXT, content);
        startActivity(intent);
    }

    private void handleViewRecords() {
        EditText nameInput = findViewById(R.id.editTextName);
        DatePicker dobInput = findViewById(R.id.DatePickerDOB);
        EditText emailInput = findViewById(R.id.editTextEmail);
        Spinner sp = findViewById(R.id.spinner);
        EditText commentsInput = findViewById(R.id.editTextComments);

        String strName = nameInput.getText().toString().trim();
        String strDOB = dobInput.getDayOfMonth() + "/" + (dobInput.getMonth() + 1) + "/" + dobInput.getYear();
        String strEmail = emailInput.getText().toString().trim();
        int row = sp.getSelectedItemPosition();
        String strStatus = (row >= 0 && row < status.length) ? status[row] : "Unknown";
        String strComments = commentsInput.getText().toString().trim();

        String summaryText = "CURRENT RECORD PREVIEW\n"
                + "-------------------------\n"
                + "Name: " + (strName.isEmpty() ? "(No name entered)" : strName) + "\n"
                + "DOB: " + strDOB + "\n"
                + "Email: " + (strEmail.isEmpty() ? "(No email entered)" : strEmail) + "\n"
                + "Work Status: " + strStatus + "\n"
                + "Comments: " + (strComments.isEmpty() ? "(None)" : strComments);

        navigateToDisplayActivity(summaryText);
    }

    private void handleClearForm() {
        ((EditText) findViewById(R.id.editTextName)).setText("");
        ((EditText) findViewById(R.id.editTextEmail)).setText("");
        ((EditText) findViewById(R.id.editTextComments)).setText("");
        ((DatePicker) findViewById(R.id.DatePickerDOB)).updateDate(2004, 1, 13);
        ((Spinner) findViewById(R.id.spinner)).setSelection(0);
        ((CheckBox) findViewById(R.id.chkAgreeTerms)).setChecked(false);

        popupToast("Form inputs reset successfully");
    }

    private void handleAbout() {
        new AlertDialog.Builder(this)
                .setTitle("About FormInputExample")
                .setMessage("FormInputExample v1.0\nICS 2300 Mobile Application Development\nDemonstrating Input Forms, Validation, Confirmation Dialogs and Navigation.")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemNext) {
            handleSubmitAndConfirm();
            return true;
        } else if (id == R.id.itemViewRecords) {
            handleViewRecords();
            return true;
        } else if (id == R.id.itemClear) {
            handleClearForm();
            return true;
        } else if (id == R.id.itemAbout) {
            handleAbout();
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
