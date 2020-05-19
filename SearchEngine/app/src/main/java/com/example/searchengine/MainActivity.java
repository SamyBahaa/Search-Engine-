package com.example.searchengine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSearch =  findViewById(R.id.searchEditText);
    }

    public void OnSearch (View v)
    {
        String Value = textSearch.getText().toString();
        if (!Value.equals("")){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("key",Value);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
