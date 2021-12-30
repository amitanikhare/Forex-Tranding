package com.amita.forextranding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email, password;
    Button logIn, promo, about;
    TextView goToSignUp;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth auth;
        FirebaseUser user;


        FirebaseApp.initializeApp(MainActivity.this);

        init();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Promo.class));
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.toString();
                String p = password.toString();

                if (e.isEmpty()) {
                    email.setError("Please enter valid email..");
                } else if (p.isEmpty()) {
                    password.setError("Please enter password..");
                } else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Logging");
                    progressDialog.setMessage("Please wait..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                Toast.makeText(MainActivity.this, "Login success...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to login " + task.getException(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void init() {
        email = findViewById(R.id.mainEmail);
        password = findViewById(R.id.mainPass);
        logIn = findViewById(R.id.login);
        promo = findViewById(R.id.promo);
        about = findViewById(R.id.about);
        goToSignUp = findViewById(R.id.signUp);
    }
}