package com.example.movie2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private MaterialButton buttonLogin;
    private CardView buttonGoogleLogin;
    private EditText editTextEmail, editTxtPassword;
    private TextView forgotPasswordTV;
    private TextView signUpTV;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        utils = new Utils(this);

        mAuth = FirebaseAuth.getInstance();
        initViews();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().isEmpty() || editTxtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty Fields are not allowed", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTxtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            utils.setSignedIn(true);

                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to login ", Toast.LENGTH_LONG).show();
                            utils.setSignedIn(false);
                        }
                    }
                });
            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Sign in with Google
                Toast.makeText(LoginActivity.this, "Not Yet", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: forgot password activity
                Toast.makeText(LoginActivity.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTxtEmail);
        editTxtPassword = findViewById(R.id.editTextPass);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);
        buttonLogin = findViewById(R.id.signInButton);
        buttonGoogleLogin = findViewById(R.id.googleSignInButton);
        signUpTV = findViewById(R.id.signUpTV);

    }

}
