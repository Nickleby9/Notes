package com.hilay.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    SharedPreferences prefs;
    Button btnPrev, btnNext;
    TextView tvTitle;
    EditText etNote;
    int counter = 1;
    int pageCounter = 1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("@@@", "onSaveInstanceState");
        outState.putInt("counter",counter);
        outState.putString("current",tvTitle.getText().toString());
        outState.putInt("pageCounter",pageCounter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("Notes", MODE_PRIVATE);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etNote = (EditText) findViewById(R.id.etNote);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        etNote.addTextChangedListener(this);

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter");
            tvTitle.setText(savedInstanceState.getString("current"));
            etNote.setText(load(savedInstanceState.getString("current")));
            pageCounter = savedInstanceState.getInt("pageCounter");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageCounter = counter;
                counter++;
                pageCounter++;
                etNote.setText("");
                tvTitle.setText("Note " + counter);
            }
        });
    }

    public void save(String id, String data) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Note " + counter, etNote.getText().toString());
        editor.apply();
    }

    private String load(String id) {
        return prefs.getString(id, "#Error2");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (counter != 0) {
            if (id == R.id.btnNext) {
                if (pageCounter + 1 <= counter) {
                    pageCounter++;
                    etNote.setText(load("Note " + pageCounter));
                    tvTitle.setText("Note " + pageCounter);
                }
            }
            if (id == R.id.btnPrev) {
                if (pageCounter - 1 > 0) {
                    pageCounter--;
                    etNote.setText(load("Note " + pageCounter));
                    tvTitle.setText("Note " + pageCounter);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        save("Note " + counter, etNote.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
