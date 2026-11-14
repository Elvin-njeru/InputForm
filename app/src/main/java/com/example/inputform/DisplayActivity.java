package com.example.inputform;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DisplayActivity extends AppCompatActivity {

    public static final String EXTRA_DISPLAY_TEXT = "display_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.display_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView3 = findViewById(R.id.textView3);
        String displayText = getIntent().getStringExtra(EXTRA_DISPLAY_TEXT);
        if (displayText != null) {
            textView3.setText(displayText);
        }
    }
}
