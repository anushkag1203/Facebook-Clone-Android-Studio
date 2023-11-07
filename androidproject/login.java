package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.regex.Pattern;
import com.example.androidproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Set the input type for password EditText to "password" to hide the characters
        binding.passLoginET.getEditText().setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailLoginET.getEditText().getText().toString().trim();
                String pass = binding.passLoginET.getEditText().getText().toString().trim();

                if (email.isEmpty()) {
                    binding.emailLoginET.setError("Email is required");
                    binding.emailLoginET.requestFocus();
                    return;
                }

                if (!isEmailValid(email)) {
                    binding.emailLoginET.setError("Enter a valid email address");
                    binding.emailLoginET.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    binding.passLoginET.setError("Password is required");
                    binding.passLoginET.requestFocus();
                    return;
                }

                if (!isPasswordValid(pass)) {
                    binding.passLoginET.setError("Password should contain at least one uppercase letter, one lowercase letter, and one number");
                    binding.passLoginET.requestFocus();
                    return;
                }

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login.this, "Authentication failed, check your email and password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
