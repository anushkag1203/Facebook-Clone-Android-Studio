package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.regex.Pattern;
import com.example.androidproject.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Set the input type for password EditText to "password" to hide the characters
        binding.passET.getEditText().setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        binding.signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailET.getEditText().getText().toString().trim();
                String pass = binding.passET.getEditText().getText().toString().trim();
                String name = binding.nameET.getEditText().getText().toString().trim();
                String address = binding.birthET.getEditText().getText().toString().trim();
                String phone = binding.phoneET.getEditText().getText().toString().trim();

                if (name.isEmpty()) {
                    binding.nameET.setError("Name is required");
                    binding.nameET.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    binding.birthET.setError("Address is required");
                    binding.birthET.requestFocus();
                    return;
                }

                if (!isEmailValid(email)) {
                    binding.emailET.setError("Enter a valid email address");
                    binding.emailET.requestFocus();
                    return;
                }

                if (!isPasswordValid(pass)) {
                    binding.passET.setError("Password should contain at least one uppercase letter, one lowercase letter, and one number");
                    binding.passET.requestFocus();
                    return;
                }

                if (!isPhoneNumberValid(phone)) {
                    binding.phoneET.setError("Phone number should contain 10 digits");
                    binding.phoneET.requestFocus();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, address, phone, email, pass);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("users").child(id).setValue(user);
                            Toast.makeText(signup.this, "Account has been created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(signup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(signup.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isPhoneNumberValid(String phone) {
        String phonePattern = "^[0-9]{10}$";
        return Pattern.matches(phonePattern, phone);
    }
}
