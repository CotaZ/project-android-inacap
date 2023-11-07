package com.cotaz.prueba;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class CreatePetActivity extends AppCompatActivity {
    Button btn_add;
    EditText name, age, color;
    private FirebaseFirestore mfirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Flecha para atras de mi Tolbar
        mfirestore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.nombre);
        age = findViewById(R.id.edad);
        color = findViewById(R.id.color);
        btn_add = findViewById(R.id.btn_add);

        this.setTitle("Crear mascota");
        btn_add.setOnClickListener(v -> {
            String namepet = name.getText().toString().trim();
            String agepet = age.getText().toString().trim();
            String colorpet = color.getText().toString().trim();

            if (namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                Toast.makeText(getApplicationContext(), "Ingresar los datos",Toast.LENGTH_SHORT).show();
            }else{
                postPet(namepet, agepet, colorpet);
            }
        });
    }
    private void postPet(String namepet, String agepet, String colorpet) {
        Map<String, Object> map;
        map = new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        mfirestore.collection("pet").add(map).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(), "Creado Exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al Ingresar", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}