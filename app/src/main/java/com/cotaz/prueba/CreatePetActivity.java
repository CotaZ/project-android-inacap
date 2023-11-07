package com.cotaz.prueba;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
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

        String id = getIntent().getStringExtra("id_pet");
        mfirestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.nombre);
        age = findViewById(R.id.edad);
        color = findViewById(R.id.color);
        btn_add = findViewById(R.id.btn_add);

        if (id == null || id == "") {
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
        }else{
            btn_add.setText("Update");
            getPet(id);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namepet = name.getText().toString().trim();
                    String agepet = age.getText().toString().trim();
                    String colorpet = color.getText().toString().trim();

                    if (namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los datos",Toast.LENGTH_SHORT).show();
                    }else{
                        updatePet(namepet, agepet, colorpet, id);
                    }
                }
            });
        }

        this.setTitle("Crear mascota");

    } //Se genera la instacia almacenamiento de datos

    private void updatePet(String namepet, String agepet, String colorpet, String id) {
        Map<String, Object> map= new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);

        mfirestore.collection("pet").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    } //Se envian los datos para actualizar la información de los documentos creados

    private void postPet(String namepet, String agepet, String colorpet) {
        Map<String, Object> map= new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        mfirestore.collection("pet").add(map).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(), "Creado Exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al Ingresar", Toast.LENGTH_SHORT).show();
        });
    } // Se muestan la lista de documento creados
    private void getPet(String id){
        mfirestore.collection("pet").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String namePet = documentSnapshot.getString("name");
                String agePet = documentSnapshot.getString("age");
                String colorPet = documentSnapshot.getString("color");

                name.setText(namePet);
                age.setText(agePet);
                color.setText(colorPet);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    } //Se obtiene toda la información al actualizar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}