package ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.github.saw47.mywe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import backup.NoteBackup;
import db.NoteDao;
import db.NoteRoomDatabase;

public class SettingsActivity extends AppCompatActivity {

    private static final String EXPORT_MIME = "application/json";
    private static final String DEFAULT_FILE_NAME = "mywe_notes.json";

    private ActivityResultLauncher<String[]> importLauncher;
    private ActivityResultLauncher<String> exportLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.back_button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        importLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        runImport(uri);
                    }
                });

        exportLauncher = registerForActivityResult(
                new ActivityResultContracts.CreateDocument(EXPORT_MIME),
                uri -> {
                    if (uri != null) {
                        runExport(uri);
                    }
                });

        findViewById(R.id.import_button).setOnClickListener(v ->
                importLauncher.launch(new String[]{"application/json", "text/plain", "*/*"}));

        findViewById(R.id.export_button).setOnClickListener(v ->
                exportLauncher.launch(DEFAULT_FILE_NAME));
    }

    private void runImport(Uri uri) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            try {
                ContentResolver resolver = getContentResolver();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        resolver.openInputStream(uri), StandardCharsets.UTF_8))) {
                    NoteDao dao = NoteRoomDatabase.getDatabase(this).noteDao();
                    int count = NoteBackup.importNotes(dao, reader);
                    runOnUiThread(() -> Toast.makeText(this,
                            getString(R.string.import_success, count), Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this,
                        R.string.import_failed, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void runExport(Uri uri) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            try {
                ContentResolver resolver = getContentResolver();
                try (OutputStreamWriter writer = new OutputStreamWriter(
                        resolver.openOutputStream(uri), StandardCharsets.UTF_8)) {
                    NoteDao dao = NoteRoomDatabase.getDatabase(this).noteDao();
                    int count = NoteBackup.exportNotes(dao, writer);
                    runOnUiThread(() -> Toast.makeText(this,
                            getString(R.string.export_success, count), Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this,
                        R.string.export_failed, Toast.LENGTH_LONG).show());
            }
        });
    }
}
