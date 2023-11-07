package com.cotaz.prueba.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cotaz.prueba.CreatePetActivity;
import com.cotaz.prueba.CreatePetFragment;
import com.cotaz.prueba.R;
import com.cotaz.prueba.model.Pet;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetAdapter extends FirestoreRecyclerAdapter<Pet, PetAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private Activity activity;
    private FragmentManager fm;

    public PetAdapter(@NonNull FirestoreRecyclerOptions<Pet> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull Pet pet) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String id = documentSnapshot.getId();

        viewHolder.name.setText(pet.getName());
        viewHolder.age.setText(String.valueOf(pet.getAge()));
        viewHolder.color.setText(pet.getColor());

        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreatePetActivity.class);
                intent.putExtra("id_pet", id);
                activity.startActivity(intent);

                // Abrir CreatePetFragment
                CreatePetFragment createPetFragment = new CreatePetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_pet", id);
                createPetFragment.setArguments(bundle);
                createPetFragment.show(fm, "open fragment");
            }
        });
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePet(id);
            }
        });
    }

    private void deletePet(String id) {
        mFirestore.collection("pet").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pet_single, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, color;
        ImageView btn_delete, btn_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            age = itemView.findViewById(R.id.edad);
            color = itemView.findViewById(R.id.color);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }
}
