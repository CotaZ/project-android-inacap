package com.cotaz.prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.os.Bundle;

public class CreatePetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTitle("Crear mascota");


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}