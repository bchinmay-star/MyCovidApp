package com.example.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mycovidapp.Models.Institutes;
import com.example.mycovidapp.databinding.ActivitySignUpBinding;
import com.example.mycovidapp.databinding.ActivitySignUpInstituteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpInstitute extends AppCompatActivity {

    ActivitySignUpInstituteBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpInstituteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpInstitute.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're registering your Institute");

        binding.btnRegisterInsti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword
                        (binding.etInstiEmail.getText().toString(),binding.etInstiPass.getText().toString()).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Institutes institute = new Institutes(
                                    binding.etInstiName.getText().toString(),
                                    binding.etInstiPhone.getText().toString(),
                                    binding.etInstiEmail.getText().toString(),
                                    binding.etInstiPass.getText().toString(),
                                    binding.etInstiPincode.getText().toString(),
                                    binding.etInstiAddress.getText().toString()
                            );
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Institutes").child(id).setValue(institute);
                            Toast.makeText(SignUpInstitute.this, "Institute registered. Welcome to the family", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpInstitute.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        binding.etExistingInsti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpInstitute.this,SignInInstitute.class);
                startActivity(intent);
            }
        });


    }
}