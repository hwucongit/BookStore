package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.teamand.bookstore.R;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtKeySearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtKeySearch = findViewById(R.id.edt_keyword_search);
        findViewById(R.id.imb_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtKeySearch.getText().toString().isEmpty())
                    search(edtKeySearch.getText().toString());
            }
        });
    }

    private void search(String key){
        Intent intent = new Intent(this,ListBookActivity.class);
        intent.putExtra("typeSearch","byBookName");
        intent.putExtra("keySearch",key);
        startActivity(intent);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        edtKeySearch.setText("");
    }
}
